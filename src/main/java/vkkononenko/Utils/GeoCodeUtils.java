package vkkononenko.Utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.util.EntityUtils;
import vkkononenko.models.*;
import vkkononenko.models.YandexGeocoder.StopCoordsYandex;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;
import java.io.Serializable;
import java.util.Objects;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class GeoCodeUtils implements Serializable {

    public static Logger log = Logger.getLogger(GeoCodeUtils.class.getName());

    public GeoCodingResult result = null;

    @PersistenceContext(name = "geodata")
    private EntityManager em;

    ObjectMapper mapper = new ObjectMapper();

    @Transactional
    public void takeStopFromYandex(Stop stop) {
        try{
            stop = renameStop(stop);

            log.info("Геокодируется остановочный пункт: " + stop.getName());

            String replace = stop.getAdress().trim().replace(" ", "+").replaceAll("\"", "");

            result = geoCodeYandex(replace);

            if(result == null){
                replace = stop.getPlace().trim().replace(",", "").replace(" ", "+").replace("-", "").replace("«", "");
                result = geoCodeYandex(replace);
                stop.getCoords().add(result);
            } else {
                stop.getCoords().add(result);
            }
            if(stop.getCoords().stream().filter(Objects::isNull).count() < stop.getCoords().size() && stop.getCoords().size() > 0) {
                log.info("Результат записан в базу: ".concat(stop.getName()));
                stop.setRequest(replace);

                stop.setCount((long) stop.getCoords().get(0).getGeoCodingObjects().size());
                stop.setLat(stop.getCoords().get(0).getGeoCodingObjects().get(0).getLat());
                stop.setLon(stop.getCoords().get(0).getGeoCodingObjects().get(0).getLon());
                stop.setAdress(ReverseGeoCodingResultYandex(stop.getCoords().get(0).getGeoCodingObjects().get(0).getLat(), stop.getCoords().get(0).getGeoCodingObjects().get(0).getLon()));
                stop.setOkato(getOkatoForObject(stop.getCoords().get(0).getGeoCodingObjects().get(0)));
                stop.setType(stop.getCoords().get(0).getGeoCodingObjects().get(0).getType());
                stop.setSourse("yandex");

                log.info("Геокодируется остановочный пункт: " + stop.getName());

                em.merge(stop);
            }
        }
        catch (Exception ex) {

        }
    }

    @Transactional
    public GeoCodingResult geoCodeYandex(String query) {
        log.info("Отправленный запрос: ".concat(query));

        try {
            HttpGet request = new HttpGet("https://geocode-maps.yandex.ru/1.x/?format=json&apikey=74c882a5-c372-4385-bc86-bb09895d7669&geocode=".concat(query).concat("&rspn=1"));
            HttpResponse response = new DefaultHttpClient().execute(request);
            HttpEntity entity = response.getEntity();
            String res = EntityUtils.toString(entity, "UTF-8");
            if(StringUtils.isBlank(res)) {
                log.info("Результат не получен");
            }
            StopCoordsYandex yandex = mapper.readValue(res, StopCoordsYandex.class);
            log.info("Результат: ".concat(res));
            GeoCodingResult result = new GeoCodingResult(yandex, query);


                result.getGeoCodingObjects().removeIf((a -> !a.getText().contains("Россия")));
                result.getGeoCodingObjects().removeIf((a -> a.getType().equals("hydro")));
                result.getGeoCodingObjects().stream().forEach(a -> { a.setSendedRequest(result.getSendedRequest());});
                if (result.getGeoCodingObjects().size() > 0) {
                    result.setCount((long) result.getGeoCodingObjects().size());
                    em.persist(result);
                    return result;
                }
        } catch(Exception ex) {

        }
        return null;
    }

    private String ReverseGeoCodingResultYandex(Double lat, Double lon) {
        HttpGet reverseRequest = new HttpGet("http://geocode-maps.yandex.ru/1.x/?geocode=".concat(Objects.toString(lat)).concat(",").concat(Objects.toString(lon)).concat("&format=json&key=74c882a5-c372-4385-bc86-bb09895d7669"));
        HttpResponse reverseResponse = null;
        try {
            reverseResponse = new DefaultHttpClient().execute(reverseRequest);
            HttpEntity reverseEntity = reverseResponse.getEntity();
            JsonNode array = mapper.readValue(EntityUtils.toString(reverseEntity), JsonNode.class);
            return array.get("response").get("GeoObjectCollection").get("featureMember").get(0).get("GeoObject").get("metaDataProperty").get("GeocoderMetaData").get("text").textValue();
        } catch (Exception ex) {

        }
        return null;
    }

    @Transactional
    public void takeStopFromNominatim(Stop stop) {
        ObjectMapper mapper = new ObjectMapper();
        try{
            String replace = stop.getName().trim().replace(" ", "+").replace("\"", "");
            Stop result = geoCodeNominatim(stop, replace);
            if(result.getCount() == null) {
                replace = stop.getPlace().trim().replace(" ", "+").replace("\"", "");
                result = geoCodeNominatim(stop, replace);
            }
            em.merge(result);
            log.info("Отправленный запрос: ".concat(replace));
        }
        catch (Exception ex) {
            log.info(ex.getMessage());
        }
    }

    public Stop geoCodeNominatim(Stop stop, String replace) {
        try {

            stop = renameStop(stop);
            log.info("Геокодируется остановочный пункт: " + stop.getName());
            HttpGet request = new HttpGet("http://nominatim.openstreetmap.org/search.php?format=json&state=".replace(" ", "+").concat("&q=").concat(replace));
            SSLContextBuilder builder = new SSLContextBuilder();
            builder.loadTrustMaterial(null, new TrustSelfSignedStrategy());
            SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(
                    builder.build());
            CloseableHttpClient httpclient = HttpClients.custom().setSSLSocketFactory(
                    sslsf).build();
            HttpResponse response = httpclient.execute(request);

            HttpEntity entity = response.getEntity();
            String res = EntityUtils.toString(entity, "UTF-8");
            JsonNode Nominatim = mapper.readValue(res, JsonNode.class);

            stop.setLat(Double.parseDouble(Nominatim.get(0).get("lat").textValue()));
            stop.setLon(Double.parseDouble(Nominatim.get(0).get("lon").textValue()));
            stop.setAdress(Nominatim.get(0).get("display_name").textValue());
            stop.setOkato(getOkato(stop.getAdress()));
            stop.setType(Nominatim.get(0).get("class").textValue());
            stop.setSourse("Nominatim");
            stop.setCount(1L);
            return stop;
        } catch (Exception ex) {
            log.info(ex.getMessage());
        }
        return null;
    }


    @Transactional
    public void setOkatoForObject(GeoCodingObject geoCodingObject) {
        try {
            String okato = getOkato(ReverseGeoCodingResultYandex(geoCodingObject.getLat(), geoCodingObject.getLon()));
            if(StringUtils.isBlank(okato)) {
                okato = getOkato(geoCodingObject.getFormatted());
            }
            geoCodingObject.setOkato(StringUtils.isNotBlank(okato) ? okato : null);
            em.merge(geoCodingObject);
            log.info("ОКАТО: ".concat(okato));
        } catch (Exception ex) {

        }
    }

    @Transactional
    public void setOkatoForStop(Stop stop) {
        try {
            String okato = getOkato(ReverseGeoCodingResultYandex(stop.getLat(), stop.getLon()));
            if(StringUtils.isBlank(okato)) {
                okato = getOkato(stop.getCoords().get(0).getGeoCodingObjects().get(0).getFormatted());
            }
            stop.setOkato(StringUtils.isNotBlank(okato) ? okato : null);
            em.merge(stop);
            log.info("ОКАТО: ".concat(okato));
        } catch (Exception ex) {

        }
    }

    @Transactional
    public String getOkatoForObject(GeoCodingObject geoCodingObject) {
        try {
            String okato = getOkato(ReverseGeoCodingResultYandex(geoCodingObject.getLat(), geoCodingObject.getLon()));
            if(StringUtils.isBlank(okato)) {
                okato = getOkato(geoCodingObject.getFormatted());
            }
            return StringUtils.isNotBlank(okato) ? okato : null;
        } catch (Exception ex) {

        }
        return null;
    }

    private String getOkato(String request) {
        try {
            String postBody = "{ \"query\": \"".concat(request).concat("\", \"count\": 10 }");
            StringEntity body = new StringEntity(postBody,
                    ContentType.APPLICATION_JSON);
            HttpPost httpPost = new HttpPost("https://suggestions.dadata.ru/suggestions/api/4_1/rs/suggest/address");
            httpPost.addHeader("Accept", "application/json");
            httpPost.addHeader("Content-Type", "application/json");
            httpPost.addHeader("Authorization", "Token 75c80d1d9cc814dd675dbbeff5c0cba1575da9de");
            httpPost.setEntity(body);
            HttpResponse okatoResponse = new DefaultHttpClient().execute(httpPost);
            HttpEntity okatoEntity = okatoResponse.getEntity();
            JsonNode array = mapper.readValue(EntityUtils.toString(okatoEntity), JsonNode.class);
            JsonNode object = array.get("suggestions");
            return object.get(0).get("data").get("okato").textValue();
        } catch (Exception ex) {

        }
        return null;
    }

    private Stop renameStop(Stop stop) {
        stop.setName(stop.getName().replace("ОП", "Остановочный пункт"));
        stop.setName(stop.getName().replace("АС", "Автостанция"));
        stop.setName(stop.getName().replace("АВ", "Автовокзал"));
        stop.setName(stop.getName().replace("ДКП", "Диспетчерский кассовый пункт"));
        stop.setName(stop.getName().replace("Остановочный пункт", ""));
        stop.setName(stop.getName().replace("Автостанция", ""));
        stop.setName(stop.getName().replace("Автовокзал", ""));
        stop.setName(stop.getName().replace("Диспетчерский кассовый пункт", ""));
        return stop;
    }

    public Double GetDistance(Double lat1, Double lon1, Double lat2, Double lon2) {
        Short R = 6371;
        Double dLat = deg2rad(lat2-lat1);
        Double dLon = deg2rad(lon2-lon1);
        Double a = Math.sin(dLat/2) * Math.sin(dLat/2) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.sin(dLon/2) * Math.sin(dLon/2);
        Double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        return (R * c);
    }

    public Double deg2rad(Double deg) {
        return deg * (Math.PI/180.0);
    }
}
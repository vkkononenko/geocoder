package vkkononenko.beans;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;
import vkkononenko.models.Region;
import vkkononenko.models.RussianFederation.FederationSubject;
import vkkononenko.models.Stop;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Named
@ViewScoped
public class RegionView implements Serializable {

    public static Logger log = Logger.getLogger(RegionView.class.getName());

    @PersistenceContext(unitName = "geodata")
    private EntityManager em;

    private UploadedFile uploadedFile;

    private XSSFWorkbook workbook;

    private List<Region> regionList;

    private ObjectMapper mapper = new ObjectMapper();

    @Transactional
    public void onLoad() {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery cq = cb.createQuery(Region.class);
        List<Predicate> predicate_list = new LinkedList<>();
        Root<Stop> root = cq.from(Region.class);
        regionList = em.createQuery(cq).getResultList();
    }

    @Transactional
    public void handleFileUpload(FileUploadEvent event) throws Exception {
        workbook = new XSSFWorkbook(event.getFile().getInputstream());
        XSSFSheet myExcelSheet = workbook.getSheet("Sheet 1");
        XSSFRow row;
        log.info("Start import");
        for (int i = 1; i < myExcelSheet.getPhysicalNumberOfRows() - 1; i++) {
            row = myExcelSheet.getRow(i);
            Region region = new Region();
            String value = row.getCell(0).getStringCellValue();
            value = value.trim();
            if(StringUtils.isNotBlank(value)) {
                region.setName(value);
            }

            value = row.getCell(1).getStringCellValue();
            value = value.trim();
            if(StringUtils.isNotBlank(value)) {
                region.setCode(value);
            }
            em.persist(region);
        }
        log.info("End import");
    }

    @Transactional
    public void getCorners() {
        log.info("Начинаем поиск координат региона");
        regionList.stream().filter(r -> r.getBiggerLat() == null && r.getLessLat() == null && r.getBiggerLon() == null && r.getLessLon() == null).collect(Collectors.toList()).forEach(r -> {
            try {
                HttpGet request = new HttpGet("https://geocode-maps.yandex.ru/1.x/?format=json&apikey=74c882a5-c372-4385-bc86-bb09895d7669&geocode=".concat(r.getName().replace(" ", "+")));
                HttpResponse response = new DefaultHttpClient().execute(request);
                HttpEntity entity = response.getEntity();
                String res = EntityUtils.toString(entity, "UTF-8");
                JsonNode jsonNode = mapper.readValue(res, JsonNode.class);
                String lowerCorner = jsonNode.get("response").get("GeoObjectCollection").get("featureMember").get(0).get("GeoObject").get("boundedBy").get("Envelope").get("lowerCorner").textValue();
                String upperCorner = jsonNode.get("response").get("GeoObjectCollection").get("featureMember").get(0).get("GeoObject").get("boundedBy").get("Envelope").get("upperCorner").textValue();
                Double lat0 = Double.parseDouble(lowerCorner.split(" ")[0]);
                Double lat1 = Double.parseDouble(upperCorner.split(" ")[0]);
                Double lon0 = Double.parseDouble(lowerCorner.split(" ")[1]);
                Double lon1 = Double.parseDouble(upperCorner.split(" ")[1]);
                r.setBiggerLat(lat0 >= lat1 ? lat0 : lat1);
                r.setLessLat(lat0 >= lat1 ? lat1 : lat0);
                r.setBiggerLon(lon0 >= lon1 ? lon0 : lon1);
                r.setLessLon(lon0 >= lon1 ? lon1 : lon0);
                r.setLowerCorner(lowerCorner.trim().replace(" ", ","));
                r.setUpperCorner(upperCorner.trim().replace(" ", ","));
                em.merge(r);
            } catch (Exception ex) {

            }
        });
        log.info("Координаты найдены");
    }

    public UploadedFile getUploadedFile() {
        return uploadedFile;
    }

    public void setUploadedFile(UploadedFile uploadedFile) {
        this.uploadedFile = uploadedFile;
    }

    public XSSFWorkbook getWorkbook() {
        return workbook;
    }

    public void setWorkbook(XSSFWorkbook workbook) {
        this.workbook = workbook;
    }

    public List<Region> getRegionList() {
        return regionList;
    }

    public void setRegionList(List<Region> regionList) {
        this.regionList = regionList;
    }
}

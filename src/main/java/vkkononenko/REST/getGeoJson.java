package vkkononenko.REST;

import com.fasterxml.jackson.databind.ObjectMapper;
import vkkononenko.models.Stop;
import vkkononenko.models.YandexMapsAPI.PointsJSON;
import javax.enterprise.context.RequestScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.util.Objects;
import java.util.logging.Logger;

/**
 * Created by v.kononenko on 30.10.2018.
 */
@Path("/getGeoJsonForStop")
@RequestScoped
public class getGeoJson
{
    public static Logger log = Logger.getLogger(getGeoJson.class.getName());

    @PersistenceContext(name = "geodata")
    private EntityManager em;

    @GET
    @Produces(value="application/json")
    @Transactional
    public String takeOwnerInfo(@QueryParam("stopId") Long stopId, @QueryParam("registered") Long registered) throws IOException {
        try {
            ObjectMapper mapper = new ObjectMapper();
            String result = null;
            if(registered == 1) {
                Stop stop = em.find(Stop.class, stopId);
                PointsJSON json = new PointsJSON(stop);
                result =  mapper.writeValueAsString(json);
            }
            log.info(result);
            return result;
        } catch (Exception ex) {
            log.info(ex.getMessage());
        }

        return null;
    }
}


package vkkononenko.beans;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;
import vkkononenko.Utils.GeoCodeUtils;
import vkkononenko.models.*;

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
import java.io.IOException;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.Logger;

@Named
@ViewScoped
public class stopsView implements Serializable {

    public static Logger log = Logger.getLogger(stopsView.class.getName());

    @PersistenceContext(unitName = "geodata")
    private EntityManager em;

    @Inject
    private GeoCodeUtils geoCodeUtils;

    private List<Stop> stopsList;

    private UploadedFile uploadedFile;

    private XSSFWorkbook workbook;

    private String geocode;

    private Long stopId;

    private Long resultId;

    @Transactional
    public void onLoad() {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery cq = cb.createQuery(Stop.class);
        List<Predicate> predicate_list = new LinkedList<>();
        Root<Stop> root = cq.from(Stop.class);
        stopsList = em.createQuery(cq).getResultList();
    }

    @Transactional
    public void handleFileUpload(FileUploadEvent event) throws Exception {
        workbook = new XSSFWorkbook(event.getFile().getInputstream());
        XSSFSheet myExcelSheet = workbook.getSheet("Остановочные пункты");
        XSSFRow row;
        log.info("Start import");
        for (int i = 1; i < myExcelSheet.getPhysicalNumberOfRows() - 1; i++) {
                row = myExcelSheet.getRow(i);
                Stop stop = new Stop();
                String value = row.getCell(0).getStringCellValue();
                value = value.trim();
                if(StringUtils.isNotBlank(value)) {
                    stop.setName(value);
                }

                value = row.getCell(1).getStringCellValue();
                value = value.trim();
                if(StringUtils.isNotBlank(value)) {
                    stop.setRegNum(value);
                    if(stopsList.stream().filter(s -> s.getRegNum().equals(stop.getRegNum())).count() != 0)
                    {
                        continue;
                    }
                    log.info("New Stop with regnum:".concat(stop.getRegNum()));
                }

                value = row.getCell(2).getStringCellValue();
                value = value.trim();
                if(StringUtils.isNotBlank(value)) {
                    log.info("Check row:" + Objects.toString(i) + " col:2");
                    String format = "dd.mm.yyyy";
                    SimpleDateFormat dateFormat = new SimpleDateFormat(format);
                    Date res = dateFormat.parse(value);
                    stop.setRegDate(res);
                }

                value = row.getCell(3).getStringCellValue();
                value = value.trim();
                if(StringUtils.isNotBlank(value)) {
                    stop.setPlace(value);
                }

                value = row.getCell(4).getStringCellValue();
                value = value.trim();
                if(StringUtils.isNotBlank(value)) {
                    stop.setOwnerName(value);
                }

                value = row.getCell(5).getStringCellValue();
                value = value.trim();
                if(StringUtils.isNotBlank(value)) {
                    stop.setCapasity(Long.parseLong(value));
                }

                value = row.getCell(6).getStringCellValue();
                value = value.trim();
                if(StringUtils.isNotBlank(value)) {
                    stop.setTimeBreak(Long.parseLong(value));
                }

                value = row.getCell(7).getStringCellValue();
                value = value.trim();
                if(StringUtils.isNotBlank(value)) {
                    stop.setPath(value);
                }
                em.persist(stop);
                stopsList.add(stop);
        }
        log.info("End import");
    }

    @Transactional
    public void deleteResults() {
        log.info("Вычищаем говно");
        stopsList.stream().forEach((Stop a) -> em.remove(em.merge(a)));
        log.info("Говно вычищено полностью");
    }

    public void geocoding() throws IOException, InterruptedException{
        log.info("Начат процесс геокодирования");
        for(Stop s : stopsList) {
            if(s.getCount() == null) {
                geoCodeUtils.takeStopFromYandex(s);
                if(s.getCount() == null) {
                    geoCodeUtils.takeStopFromNominatim(s);
                    Thread.sleep(2000);
                }
            }
        }
        log.info("Процесс геокодирования завершен");
    }

    public void geocodeById() {
        stopsList.stream().filter(s -> s.getId().equals(stopId)).forEach(s -> {
            try {
                geoCodeUtils.takeStopFromYandex(s);
            } catch (Exception ex) {

            }
        });
    }

    public void getOkato() {
        log.info("Начат процесс вычисления ОКАТО");
        stopsList.stream().filter(s -> s.getOkato() == null).forEach(s ->  {
            try {
                geoCodeUtils.setOkatoForStop(s);
            } catch (Exception ex) {
                log.info(ex.getMessage());
            }
        });
        log.info("Процесс вычисления ОКАТО завершен");
    }

    public boolean disableMap(Stop stop) {
        return stop.getCount() == null;
    }

    public void setParamsForYandexMap(Long stopId) {
        this.stopId = stopId;
    }

    public List<Stop> getStopsList() {
        return stopsList;
    }

    public void setStopsList(List<Stop> stopsList) {
        this.stopsList = stopsList;
    }

    public UploadedFile getUploadedFile() {
        return uploadedFile;
    }

    public void setUploadedFile(UploadedFile uploadedFile) {
        this.uploadedFile = uploadedFile;
    }

    public Long getStopId() {
        return stopId;
    }

    public void setStopId(Long stopId) {
        this.stopId = stopId;
    }

    public String getGeocode() {
        return geocode;
    }

    public void setGeocode(String geocode) {
        this.geocode = geocode;
    }

    public Long getResultId() {
        return resultId;
    }

    public void setResultId(Long resultId) {
        this.resultId = resultId;
    }
}

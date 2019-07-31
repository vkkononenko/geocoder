package vkkononenko.beans;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;
import vkkononenko.Utils.GeoCodeUtils;
import vkkononenko.models.Stop;
import vkkononenko.models.UnregisteredStop;

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
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.logging.Logger;

@Named
@ViewScoped
public class unregisteredStopsView implements Serializable {

    public static Logger log = Logger.getLogger(stopsView.class.getName());

    @PersistenceContext(unitName = "geodata")
    private EntityManager em;

    @Inject
    private GeoCodeUtils geoCodeUtils;

    private List<UnregisteredStop> unregisteredStopList;

    private UploadedFile uploadedFile;

    private XSSFWorkbook workbook;

    private String geocode;

    private Long stopId;

    private Long resultId;

    @Transactional
    public void onLoad() {
        CriteriaBuilder cb1 = em.getCriteriaBuilder();
        CriteriaQuery cq1 = cb1.createQuery(UnregisteredStop.class);
        List<Predicate> predicate_list1 = new LinkedList<>();
        Root<Stop> root1 = cq1.from(UnregisteredStop.class);
        unregisteredStopList = em.createQuery(cq1).getResultList();
    }

    @Transactional
    public void handleFileUpload(FileUploadEvent event) throws Exception {
        BufferedInputStream buf = new BufferedInputStream(event.getFile().getInputstream());
        workbook = new XSSFWorkbook(buf);
        XSSFSheet myExcelSheet = workbook.getSheet("Лист1");
        XSSFRow row;
        log.info("Start import");
        for (int i = 1; i < myExcelSheet.getPhysicalNumberOfRows() - 1; i++) {
            row = myExcelSheet.getRow(i);
            UnregisteredStop stop = new UnregisteredStop();
            String value = row.getCell(0).getCellType() == 1 ? row.getCell(0).getStringCellValue()
                    : Objects.toString(row.getCell(0).getNumericCellValue());
            value = value.trim();
            if(StringUtils.isNotBlank(value)) {
                stop.setRegNum(value);
            }

            String value1 = row.getCell(1).getStringCellValue();
            value1 = value1.trim();
            if(StringUtils.isNotBlank(value1)) {
                stop.setName(value1);
            }
            em.persist(stop);
        }
        log.info("End import");
    }

    @Transactional
    public void deleteResults() {
        log.info("Вычищаем говно");
        unregisteredStopList.forEach((UnregisteredStop a) -> em.remove(em.merge(a)));
        log.info("Говно вычищено полностью");
    }

    public void geocoding() throws IOException {
        log.info("Начат процесс геокодирования");
        unregisteredStopList.stream().filter(s -> s.getCoords().size() == 0).forEach(s -> {
            try {
                geoCodeUtils.takeStopFromYandex(s);
            } catch (Exception ex) {
                log.info(ex.getMessage());
            }
        });
        log.info("Процесс геокодирования завершен");
    }

    public void getOkato() {
        log.info("Начат процесс вычисления ОКАТО");
        unregisteredStopList.stream().filter(s -> s.getCoords().size() > 0).forEach(s -> s.getCoords().forEach(r -> r.getGeoCodingObjects().stream().filter(o -> StringUtils.isBlank(o.getOkato())).forEach(o -> {
            try {
                geoCodeUtils.setOkatoForObject(o);
            } catch (Exception ex) {
                log.info(ex.getMessage());
            }
        })));
        log.info("Процесс вычисления ОКАТО завершен");
    }

    public boolean disableMap(UnregisteredStop stop) {
        return !(stop.getCoords().size() > 0);
    }

    public void setParamsForYandexMap(Long stopId, Long resultId) {
        this.stopId = stopId;
        this.resultId = resultId;
    }

    public List<UnregisteredStop> getUnregisteredStopList() {
        return unregisteredStopList;
    }

    public void setUnregisteredStopList(List<UnregisteredStop> unregisteredStopList) {
        this.unregisteredStopList = unregisteredStopList;
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

    public String getGeocode() {
        return geocode;
    }

    public void setGeocode(String geocode) {
        this.geocode = geocode;
    }

    public Long getStopId() {
        return stopId;
    }

    public void setStopId(Long stopId) {
        this.stopId = stopId;
    }

    public Long getResultId() {
        return resultId;
    }

    public void setResultId(Long resultId) {
        this.resultId = resultId;
    }
}

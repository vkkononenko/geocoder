package vkkononenko.Utils;

import vkkononenko.models.Component;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class DataUtils implements Serializable {

    @PersistenceContext(name = "geodata")
    private static EntityManager em;

    private static List<Component> componentList = new ArrayList<>();

    @PostConstruct
    @Transactional
    public void init() {
        Query q = em.createQuery("select c from Component c");
        componentList = q.getResultList();
    }

    public static boolean equalsComponents(Component component) {
        if(componentList.stream().filter((Component a) -> a.getName().equals(component.getName())).count() > 1) {
            return true;
        } else {
            componentList.add(component);
            em.persist(component);
            return false;
        }
    }
}

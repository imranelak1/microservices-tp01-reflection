package metier;
import dao.IDao;
import java.util.Objects;
/** Double une valeur sans connaître la classe concrète du DAO. */
public class MetierImpl implements IMetier {
    private IDao dao;
    public void setDao(IDao dao) { this.dao = Objects.requireNonNull(dao); }
    public double calcul() {
        if (dao == null) throw new IllegalStateException("Le DAO n'a pas été injecté");
        double result = dao.getValue() * 2;
        System.out.println("[METIER] Calcul effectué : " + result);
        return result;
    }
}

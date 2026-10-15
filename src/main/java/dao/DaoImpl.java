package dao;
/** Source de données simulée dans le TP. */
public class DaoImpl implements IDao {
    public double getValue() { System.out.println("[DAO] Lecture de la valeur : 100.0"); return 100.0; }
}

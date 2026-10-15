package presentation;

import dao.IDao;
import metier.IMetier;
import java.nio.file.*;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.List;

/** Petit conteneur : lecture, instanciation et injection au démarrage. */
public final class Presentation2 {
  public static void main(String[] args) throws Exception {
    IMetier metier = load(args.length == 0 ? Path.of("config.txt") : Path.of(args[0]));
    System.out.println("Résultats = " + metier.calcul());
  }

  public static IMetier load(Path config) throws Exception {
    String[] names = loadClassNames(config);
    IDao dao = IDao.class.cast(instantiateClass(names[0]));
    IMetier metier = IMetier.class.cast(instantiateClass(names[1]));
    System.out.println("[CONTAINER] DAO instancié : " + dao.getClass().getName());
    System.out.println("[CONTAINER] Métier instancié : " + metier.getClass().getName());
    injectDependency(metier, "setDao", IDao.class, dao);
    return metier;
  }

  /** Refuse une configuration incomplète au lieu d'une erreur null tardive. */
  public static String[] loadClassNames(Path file) throws IOException {
    List<String> lines = Files.readAllLines(file).stream().map(String::trim).toList();
    if (lines.size() != 2 || lines.stream().anyMatch(String::isBlank))
      throw new IllegalArgumentException(
          "Le fichier doit contenir exactement deux noms de classes");
    return lines.toArray(String[]::new);
  }

  public static Object instantiateClass(String name) throws ReflectiveOperationException {
    return Class.forName(name).getDeclaredConstructor().newInstance();
  }

  /** Recherche le setter par son contrat, même si le DAO possède plusieurs interfaces. */
  public static void injectDependency(Object target, String setter, Class<?> contract, Object value)
      throws ReflectiveOperationException {
    Method method = target.getClass().getMethod(setter, contract);
    method.invoke(target, value);
  }
}

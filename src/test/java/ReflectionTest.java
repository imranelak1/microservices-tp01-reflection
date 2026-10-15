import org.junit.jupiter.api.*;
import org.junit.jupiter.api.io.TempDir;
import java.nio.file.*;
import metier.MetierImpl;
import presentation.Presentation2;
import static org.junit.jupiter.api.Assertions.*;
class ReflectionTest {
    @TempDir Path dir;
    Path config(String text) throws Exception { Path p=dir.resolve("config.txt"); Files.writeString(p,text); return p; }
    @Test void chargeEtInjecteParReflexion() throws Exception { assertEquals(200,Presentation2.load(config("dao.DaoImpl\nmetier.MetierImpl")).calcul()); }
    @Test void changeLaSourceSansModifierLeMetier() throws Exception { assertEquals(300,Presentation2.load(config("dao.DaoImpl2\nmetier.MetierImpl")).calcul()); }
    @Test void refuseClasseAbsente() throws Exception { Path p=config("dao.Absente\nmetier.MetierImpl");assertThrows(ClassNotFoundException.class,()->Presentation2.load(p)); }
    @Test void refuseContratIncompatible() throws Exception { Path p=config("java.lang.String\nmetier.MetierImpl");assertThrows(ClassCastException.class,()->Presentation2.load(p)); }
    @Test void refuseConfigurationIncomplete() throws Exception { Path p=config("dao.DaoImpl");assertThrows(IllegalArgumentException.class,()->Presentation2.load(p)); }
    @Test void signaleDependanceNonInjectee() { assertThrows(IllegalStateException.class,()->new MetierImpl().calcul()); }
}

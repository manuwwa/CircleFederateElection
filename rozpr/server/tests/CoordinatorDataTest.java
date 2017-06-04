import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

/**
 * Created by Marcin on 30.05.2017.
 */
@RunWith(Arquillian.class)
public class CoordinatorDataTest {
    @Test
    public void getRealPriority() throws Exception {
    }

    @Test
    public void equals() throws Exception {
    }

    @Deployment
    public static JavaArchive createDeployment() {
        return ShrinkWrap.create(JavaArchive.class)
                .addClass(CoordinatorData.class)
                .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
    }

}

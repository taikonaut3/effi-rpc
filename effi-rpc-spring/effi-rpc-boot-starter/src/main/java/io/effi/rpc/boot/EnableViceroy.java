package io.effi.rpc.boot;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * Enable Virtue Rpc.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import({VirtueRegister.class})
public @interface EnableViceroy {

    /**
     * Scan base packages include client and server.
     *
     * @return
     */
    String[] scanBasePackages() default {};

    /**
     * Scan base packages include client.
     *
     * @return
     */
    String[] clientScan() default {};

    /**
     * Scan base packages include server.
     *
     * @return
     */
    String[] serverScan() default {};

}

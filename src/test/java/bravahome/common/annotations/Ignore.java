/*
 * *********************************************************
 *  Copyright (c) 2017 Brava Home Inc.  All rights reserved.
 * *********************************************************
 */
package bravahome.common.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/*
 * Created by gzoldi on 05/03/2017 06:44 PM
 */
@Retention( RUNTIME )
@Target( {METHOD, TYPE} )
public @interface Ignore {
    /**
     * Describe the reason for skiping test, e.g., @Ignore(reason="Known Issue: Tax is $0.00 for CA state.")
     * @return default value
     */
    public String reason() default "";

    /**
     * Provide Jira issue ID, if available, e.g., @Ignore(bug="FX-2583")
     * @return default value
     */
    public String bug() default "";
}
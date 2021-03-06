package org.transmartproject.batch.highdim.platform

import com.google.common.collect.ImmutableList
import com.google.common.collect.ImmutableSet
import groovy.transform.TypeChecked
import org.springframework.context.i18n.LocaleContextHolder
import org.transmartproject.batch.startup.ExternalJobParametersInternalInterface
import org.transmartproject.batch.startup.ExternalJobParametersModule
import org.transmartproject.batch.startup.InvalidParametersFileException
import org.transmartproject.batch.startup.JobSpecification

/**
 * Platform for loading an annotation.
 */
@TypeChecked
abstract class AbstractPlatformJobSpecification
        implements JobSpecification, ExternalJobParametersModule {
    public final static String PLATFORM = 'PLATFORM'
    public final static String TITLE = 'TITLE'
    public final static String ORGANISM = 'ORGANISM'
    public final static String ANNOTATIONS_FILE = 'ANNOTATIONS_FILE'
    public final static String MARKER_TYPE = 'MARKER_TYPE'

    private final static String DEFAULT_ORGANISM = 'Homo Sapiens'

    final List<? extends ExternalJobParametersModule> jobParametersModules =
            ImmutableList.of(this)

    Set<String> supportedParameters = ImmutableSet.of(
            PLATFORM,
            TITLE,
            ORGANISM,
            ANNOTATIONS_FILE,
            MARKER_TYPE,)

    void validate(ExternalJobParametersInternalInterface ejp)
            throws InvalidParametersFileException {
        ejp.with {
            mandatory PLATFORM
            mandatory TITLE
            mandatory ANNOTATIONS_FILE
        }
        if (ejp[MARKER_TYPE]) {
            throw new InvalidParametersFileException(
                    "Parameter $MARKER_TYPE is not user settable")
        }
    }

    abstract String getMarkerType()

    void munge(ExternalJobParametersInternalInterface ejp)
            throws InvalidParametersFileException {
        ejp[ANNOTATIONS_FILE] = ejp.convertRelativePath ANNOTATIONS_FILE

        ejp[ORGANISM] = ejp[ORGANISM] ?: DEFAULT_ORGANISM

        ejp[MARKER_TYPE] = markerType

        ejp[PLATFORM] = ejp[PLATFORM]?.toUpperCase(LocaleContextHolder.locale)
    }
}

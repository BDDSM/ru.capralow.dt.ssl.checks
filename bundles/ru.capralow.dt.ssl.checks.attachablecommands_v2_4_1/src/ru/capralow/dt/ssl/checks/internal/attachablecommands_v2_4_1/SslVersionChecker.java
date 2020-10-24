/**
 * Copyright (c) 2020, Alexander Kapralov
 */
package ru.capralow.dt.ssl.checks.internal.attachablecommands_v2_4_1;

import java.text.MessageFormat;

import com._1c.g5.v8.dt.bsl.model.DynamicFeatureAccess;
import com._1c.g5.v8.dt.bsl.model.Method;
import com._1c.g5.v8.dt.bsl.model.SimpleStatement;
import com._1c.g5.v8.dt.bsl.model.Statement;
import com._1c.g5.v8.dt.bsl.model.StringLiteral;
import com._1c.g5.v8.dt.core.platform.IV8Project;
import com._1c.g5.v8.dt.metadata.mdclass.CommonModule;
import com._1c.g5.v8.dt.metadata.mdclass.MdObject;

public class SslVersionChecker
{

    public static boolean checkSslVersion(IV8Project v8Project, String startVersion)
    {
        String sslVersion = getSSLVersion(v8Project);

        return compareVersions(startVersion, sslVersion) != 1;
    }

    public static boolean checkSslVersion(IV8Project v8Project, String startVersion, String endVersion)
    {
        String sslVersion = getSSLVersion(v8Project);

        return !(compareVersions(startVersion, sslVersion) == 1 || compareVersions(endVersion, sslVersion) == -1);
    }

    private static Integer compareVersions(String version1, String version2)
    {

        String[] levels1 = version1.split("\\."); //$NON-NLS-1$
        String[] levels2 = version2.split("\\."); //$NON-NLS-1$

        Integer length = Math.max(levels1.length, levels2.length);
        for (Integer i = 0; i < length; i++)
        {
            Integer v1 = i < levels1.length ? Integer.parseInt(levels1[i]) : 0;
            Integer v2 = i < levels2.length ? Integer.parseInt(levels2[i]) : 0;
            Integer compare = v1.compareTo(v2);
            if (compare != 0)
            {
                return compare;
            }
        }

        return 0;
    }

    private static String getSSLVersion(IV8Project v8Project)
    {
        String version = ""; //$NON-NLS-1$

        MdObject configurationObject = MdObjects.getConfigurationObject(
            MessageFormat.format(MdObjects.CONFIGURATION_OBJECT, "ОбщийМодуль", "ОбновлениеИнформационнойБазыБСП"), //$NON-NLS-2$
            v8Project);
        if (configurationObject == null)
            return version;

        CommonModule mdCommonModule = (CommonModule)configurationObject;

        Method mdMethod = MdObjects.getMethod(mdCommonModule.getModule(), "ПриДобавленииПодсистемы"); //$NON-NLS-1$
        if (mdMethod == null)
            return version;

        for (Statement mdStatement : mdMethod.getStatements())
        {
            DynamicFeatureAccess methodAccess = (DynamicFeatureAccess)((SimpleStatement)mdStatement).getLeft();

            if (methodAccess.getName().equalsIgnoreCase("Версия")) //$NON-NLS-1$
            {
                version = ((StringLiteral)((SimpleStatement)mdStatement).getRight()).getLines().get(0);
                version = version.substring(1, version.length() - 1);
                break;
            }
        }

        return version;
    }

    private SslVersionChecker()
    {
        throw new IllegalStateException(Messages.Internal_class);
    }
}

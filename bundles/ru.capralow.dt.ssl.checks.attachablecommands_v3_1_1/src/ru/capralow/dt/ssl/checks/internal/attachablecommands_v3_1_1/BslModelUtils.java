/**
 * Copyright (c) 2020, Alexander Kapralov
 */
package ru.capralow.dt.ssl.checks.internal.attachablecommands_v3_1_1;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EcoreFactory;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.xtext.EcoreUtil2;
import org.eclipse.xtext.resource.DerivedStateAwareResource;
import org.eclipse.xtext.resource.IResourceServiceProvider;

import com._1c.g5.v8.dt.bsl.model.Conditional;
import com._1c.g5.v8.dt.bsl.model.DynamicFeatureAccess;
import com._1c.g5.v8.dt.bsl.model.EmptyStatement;
import com._1c.g5.v8.dt.bsl.model.Expression;
import com._1c.g5.v8.dt.bsl.model.FeatureAccess;
import com._1c.g5.v8.dt.bsl.model.FeatureEntry;
import com._1c.g5.v8.dt.bsl.model.FormalParam;
import com._1c.g5.v8.dt.bsl.model.IfStatement;
import com._1c.g5.v8.dt.bsl.model.Invocation;
import com._1c.g5.v8.dt.bsl.model.Method;
import com._1c.g5.v8.dt.bsl.model.SimpleStatement;
import com._1c.g5.v8.dt.bsl.model.SourceObjectLinkProvider;
import com._1c.g5.v8.dt.bsl.model.Statement;
import com._1c.g5.v8.dt.bsl.model.StaticFeatureAccess;
import com._1c.g5.v8.dt.bsl.model.StringLiteral;
import com._1c.g5.v8.dt.bsl.resource.DynamicFeatureAccessComputer;
import com._1c.g5.v8.dt.core.platform.IV8Project;
import com._1c.g5.v8.dt.mcore.DerivedProperty;
import com._1c.g5.v8.dt.mcore.Environmental;
import com._1c.g5.v8.dt.md.resource.MdTypeUtil;
import com._1c.g5.v8.dt.metadata.mdclass.BasicDbObject;
import com._1c.g5.v8.dt.metadata.mdclass.CommonModule;
import com._1c.g5.v8.dt.metadata.mdclass.Subsystem;

public class BslModelUtils
{

    private static DynamicFeatureAccessComputer dynamicFeatureAccessComputer =
        IResourceServiceProvider.Registry.INSTANCE.getResourceServiceProvider(URI.createURI("foo.bsl")).get( //$NON-NLS-1$
            DynamicFeatureAccessComputer.class);

    public static void parseStatements(Method method, List<String> objectsList, IV8Project v8Project)
    {
        EList<FormalParam> methodParams = method.getFormalParams();
        if (methodParams.isEmpty())
            return;

        Map<String, String> modulesAliases = new HashMap<>(); // Поддержка ОбщегоНазначения.ОбщийМодуль()

        String variableName = methodParams.get(0).getName();

        for (Statement statement : method.getStatements())
            if (statement instanceof IfStatement)
                parseIfStatement(statement, variableName, objectsList, modulesAliases, v8Project);
            else
                parseSimpleStatement(statement, variableName, objectsList, modulesAliases, v8Project);
    }

    private static void parseIfStatement(Statement statement, String variableName, List<String> objectsList,
        Map<String, String> modulesAliases, IV8Project v8Project)
    {
        IfStatement ifStatement = (IfStatement)statement;

        boolean trueStatement = parseSubsystemExistsStatement(ifStatement, v8Project);

        if (!trueStatement)
            return;

        Conditional ifPart = ifStatement.getIfPart();
        for (Statement ifPartStatement : ifPart.getStatements())
            parseSimpleStatement(ifPartStatement, variableName, objectsList, modulesAliases, v8Project);
    }

    private static void parseMethodInAnotherModule(DynamicFeatureAccess dynamicMethodAccess, List<String> objectsList,
        Map<String, String> modulesAliases, IV8Project v8Project)
    {
        Method method = null;

        List<FeatureEntry> featureEntries = dynamicFeatureAccessComputer.resolveObject(dynamicMethodAccess,
            EcoreUtil2.getContainerOfType(dynamicMethodAccess, Environmental.class).environments());
        if (featureEntries.isEmpty())
        {
            // "Не установлен плагин SSL Support."
            CommonModule commonModule =
                (CommonModule)MdUtils.getMdObject(MessageFormat.format(MdUtils.MD_OBJECT, "ОбщийМодуль", //$NON-NLS-1$
                    modulesAliases.get(((FeatureAccess)dynamicMethodAccess.getSource()).getName())), v8Project);

            if (commonModule == null)
                return;

            method = MdUtils.getMethod(commonModule.getModule(), dynamicMethodAccess.getName());
            if (method == null)
                return;

        }
        else
        {
            FeatureEntry featureEntry = featureEntries.get(0);
            EObject feature = featureEntry.getFeature();

            EObject newObject = EcoreFactory.eINSTANCE.createEObject();
            ((InternalEObject)newObject).eSetProxyURI(((SourceObjectLinkProvider)feature).getSourceUri());
            method = (Method)EcoreUtil.resolve(newObject, MdUtils.getConfigurationForProject(v8Project));
            if (method.eResource() instanceof DerivedStateAwareResource)
                ((DerivedStateAwareResource)method.eResource()).installDerivedState(false);

        }

        parseStatements(method, objectsList, v8Project);
    }

    private static void parseMethodInSameModule(StaticFeatureAccess staticMethodAccess, List<String> objectsList,
        IV8Project v8Project)
    {
        List<FeatureEntry> featureEntries = dynamicFeatureAccessComputer.resolveObject(staticMethodAccess,
            EcoreUtil2.getContainerOfType(staticMethodAccess, Environmental.class).environments());
        if (featureEntries.isEmpty())
            return;

        FeatureEntry featureEntry = featureEntries.get(0);
        EObject feature = featureEntry.getFeature();

        Method method = (Method)feature;

        parseStatements(method, objectsList, v8Project);
    }

    private static void parseSimpleStatement(Statement statement, String variableName, List<String> objectsList,
        Map<String, String> modulesAliases, IV8Project v8Project)
    {
        if (statement instanceof EmptyStatement)
            return;

        SimpleStatement simpleStatement = (SimpleStatement)statement;
        Expression leftStatement = simpleStatement.getLeft();

        if (leftStatement instanceof StaticFeatureAccess)
        {
            String moduleAlias = ((StaticFeatureAccess)leftStatement).getName();

            String moduleName =
                ((StringLiteral)((Invocation)simpleStatement.getRight()).getParams().get(0)).getLines().get(0).replace(
                    "\"", ""); //$NON-NLS-1$ //$NON-NLS-2$

            modulesAliases.put(moduleAlias, moduleName);

        }
        else
        {
            Invocation leftInvocation = (Invocation)leftStatement;

            FeatureAccess methodAccess = leftInvocation.getMethodAccess();

            if (methodAccess instanceof DynamicFeatureAccess)
            {

                DynamicFeatureAccess dynamicMethodAccess = (DynamicFeatureAccess)methodAccess;
                StaticFeatureAccess source = (StaticFeatureAccess)dynamicMethodAccess.getSource();

                if (source.getName().equalsIgnoreCase(variableName))
                {
                    EList<Expression> params = leftInvocation.getParams();
                    DynamicFeatureAccess firstParam = (DynamicFeatureAccess)params.get(0);

                    List<FeatureEntry> featureEntries = dynamicFeatureAccessComputer.resolveObject(firstParam,
                        EcoreUtil2.getContainerOfType(firstParam, Environmental.class).environments());
                    if (featureEntries.isEmpty())
                        return;
                    FeatureEntry featureEntry = featureEntries.get(0);
                    DerivedProperty deriveredProperty = (DerivedProperty)featureEntry.getFeature();
                    BasicDbObject objectOwner =
                        EcoreUtil2.getContainerOfType(deriveredProperty.getSource(), BasicDbObject.class);

                    objectsList.add(MdTypeUtil.getRefType(objectOwner).getName());
                }
                else
                    parseMethodInAnotherModule(dynamicMethodAccess, objectsList, modulesAliases, v8Project);
            }
            else
            {
                parseMethodInSameModule((StaticFeatureAccess)methodAccess, objectsList, v8Project);

            }

        }
    }

    private static Boolean parseSubsystemExistsStatement(IfStatement ifStatement, IV8Project v8Project)
    {
        Boolean trueStatement = true;

        Conditional ifPart = ifStatement.getIfPart();
        Invocation predicate = (Invocation)ifPart.getPredicate();

        FeatureAccess methodAccess = predicate.getMethodAccess();
        DynamicFeatureAccess dynamicMethodAccess = (DynamicFeatureAccess)methodAccess;

        if (dynamicMethodAccess.getName().equals("ПодсистемаСуществует")) //$NON-NLS-1$
        {
            StringLiteral subsystemLiteral = (StringLiteral)predicate.getParams().get(0);

            StringBuilder subsystemName = new StringBuilder();
            subsystemName.append("Подсистема"); //$NON-NLS-1$

            for (String stringPart : subsystemLiteral.getLines().get(0).replace("\"", "").split("[.]")) //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
                subsystemName.append(".").append(stringPart); //$NON-NLS-1$

            Subsystem subsystem = (Subsystem)MdUtils.getMdObject(subsystemName.toString(), v8Project);

            trueStatement = subsystem != null;
        }

        return trueStatement;
    }

    private BslModelUtils()
    {
        throw new IllegalStateException(Messages.Internal_class);
    }
}

package dimasnaresh.lib.objectruleparser.logicparser;

import dimasnaresh.lib.StringUtils;
import dimasnaresh.lib.objectruleparser.reflection.NestedReflection;
import dimasnaresh.lib.objectruleparser.reflection.Reflection;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

public class RuleParser {

    private ScriptEngine scriptEngine;
    private Reflection reflection;

    public RuleParser() {
        ScriptEngineManager seManager = new ScriptEngineManager();
        this.reflection = new NestedReflection();
        this.scriptEngine = seManager.getEngineByName("graal.js");

    }

    public RuleParser(ScriptEngine scriptEngine) {
        this.scriptEngine = scriptEngine;
        this.reflection = new NestedReflection();
    }

    public RuleParser(Reflection reflection) {
        ScriptEngineManager seManager = new ScriptEngineManager();
        this.reflection = reflection;
        this.scriptEngine = seManager.getEngineByName("graal.js");
    }

    public RuleParser(Reflection reflection, ScriptEngine scriptEngine) {
        this.scriptEngine = scriptEngine;
        this.reflection = reflection;
    }

    /**
     *
     * @param bean
     * @param rule
     * @return
     * @throws ScriptException
     */
    public boolean parseBussinessRule(Object bean, String rule) throws ScriptException {
        String[] ruleTokens = rule.split("\\s+(?![^<>]*>)");

        for (int i=0; i<ruleTokens.length; i++) {
            if (ruleTokens[i].contains("<")){
                Object ruleToken = replacingBeanValues(bean, ruleTokens[i]);
                ruleTokens[i] = ruleToken != null ? ruleToken.toString() : "null";
            }
        }
        return (Boolean) scriptEngine.eval(String.join(" ", ruleTokens));
    }

    /**
     *
     * @param bean
     * @param ruleToken
     * @return
     */
    private Object replacingBeanValues (Object bean, String ruleToken) {
        String takeOutParenthess = ruleToken;
        takeOutParenthess = takeOutParenthess.substring(takeOutParenthess.indexOf('<') + 1);
        takeOutParenthess = takeOutParenthess.substring(0, takeOutParenthess.indexOf('>'));
        Object tokenValue = reflection.getObject(bean, takeOutParenthess);
        if ((tokenValue != null) && (tokenValue.toString().equalsIgnoreCase("true")
                || tokenValue.toString().equalsIgnoreCase("false")
                || StringUtils.isNumeric(tokenValue.toString())))
            return tokenValue;
        else
            return tokenValue != null ? "'".concat(tokenValue.toString()).concat("'"):null;
    }
}

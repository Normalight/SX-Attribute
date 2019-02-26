package github.saukiya.sxattribute.data.attribute;

import lombok.Getter;

import java.util.stream.IntStream;

/**
 * 实体属性存储区
 *
 * @author Saukiya
 */
public class SXAttributeData {

    @Getter
    private double combatPower = 0D;

    @Getter
    private double[][] values = new double[SXAttributeManager.getSubAttributes().length][];

    public SXAttributeData() {
        for (SubAttribute attribute : SXAttributeManager.getSubAttributes()) {
            values[attribute.getPriority()] = new double[attribute.getLength()];
        }
    }

    public double[] getValues(String attributeName) {
        SubAttribute subAttribute = SXAttributeManager.getSubAttribute(attributeName);
        return subAttribute != null ? getValues()[subAttribute.getPriority()] : new double[12];
    }

    public double[] getValues(SubAttribute subAttribute) {
        return subAttribute != null ? getValues()[subAttribute.getPriority()] : new double[12];
    }

    public boolean isValid() {
        return IntStream.range(0, getValues().length).anyMatch(i -> IntStream.range(0, getValues()[i].length).anyMatch(i1 -> getValues()[i][i1] != 0));
    }

    public boolean isValid(String attributeName) {
        double[] values = getValues(attributeName);
        return IntStream.range(0, values.length).anyMatch(i -> values[i] != 0);
    }

    public boolean isValid(SubAttribute attribute) {
        double[] values = getValues(attribute);
        return IntStream.range(0, values.length).anyMatch(i -> values[i] != 0);
    }

    /**
     * 增加另一个SXAttributeData的数据
     *
     * @param attributeData SXAttributeData
     * @return this
     */
    public SXAttributeData add(SXAttributeData attributeData) {
        if (attributeData != null && attributeData.isValid()) {
            for (int i = 0; i < getValues().length; i++) {
                for (int i1 = 0; i1 < getValues()[i].length; i1++) {
                    getValues()[i][i1] += attributeData.getValues()[i][i1];
                }
            }
        }
        return this;
    }

    /**
     * 减去另一个SXAttributeData的数据
     *
     * @param attributeData SXAttributeData
     * @return this
     */
    public SXAttributeData take(SXAttributeData attributeData) {
        if (attributeData != null && attributeData.isValid()) {
            for (int i = 0; i < getValues().length; i++) {
                for (int i1 = 0; i1 < getValues()[i].length; i1++) {
                    getValues()[i][i1] -= attributeData.getValues()[i][i1];
                }
            }
        }
        return this;
    }

    /**
     * 将属性计算成战斗点数
     *
     * @return double 战斗点数
     */
    public double calculationCombatPower() {
        this.combatPower = 0D;
        for (SubAttribute attribute : SXAttributeManager.getSubAttributes()) {
            this.combatPower += attribute.calculationCombatPower(getValues()[attribute.getPriority()]);
        }
        return this.combatPower;
    }

    /**
     * 纠正数据范围
     */
    public void correct() {
        for (SubAttribute attribute : SXAttributeManager.getSubAttributes()) {
            attribute.correct(getValues()[attribute.getPriority()]);
        }
    }

    @Override
    public SXAttributeData clone() {
        SXAttributeData clone = new SXAttributeData();
        clone.values = getValues().clone();
        clone.combatPower = getCombatPower();
        return clone;
    }
}

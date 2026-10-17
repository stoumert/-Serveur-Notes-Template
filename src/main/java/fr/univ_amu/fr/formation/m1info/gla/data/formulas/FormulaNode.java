package fr.univ_amu.fr.formation.m1info.gla.data.formulas;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = MaxNode.class, name = "max"),
        @JsonSubTypes.Type(value = SumNode.class, name = "sum"),
        @JsonSubTypes.Type(value = VariableNode.class, name = "variable"),
        @JsonSubTypes.Type(value = WeightNode.class, name = "weight")
})
public interface FormulaNode {
    <R> R accept(FormulaNodeVisitor<R> visitor);
}

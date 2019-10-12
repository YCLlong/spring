package cn.ycl.study.spel;

import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;

public class SpElTest {
    public static void main(String[] args) {
        ExpressionParser parser = new SpelExpressionParser();
        Expression expression = parser.parseExpression("'hello world'");
        expression = parser.parseExpression("'hello world'.bytes");
        expression = parser.parseExpression("'hello world'.bytes.length");
    }
}

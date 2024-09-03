package org.braun.digikam.backend.search.sql;

import org.braun.digikam.backend.search.ConditionParseException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import jakarta.persistence.Query;

/**
 * SQL-Condition
 * 
 * @author mbraun
 */
public class Condition<T> implements ICondition {

    private final Class<T> clazz;
    private final List<Predicate> predicates;
    private final boolean empty;

    public Condition(String columnName, String value, Class<T> clazz) throws ConditionParseException {
        this.clazz = clazz;
        empty = (value == null || value.isEmpty() || "null".equalsIgnoreCase(value));
        predicates = parse(columnName, value);
    }

    @Override
    public String getCondition() {
        boolean isFirst = true;
        StringBuilder b = new StringBuilder();
        for (Predicate<?> p : predicates.stream().filter(p -> p.isIsNot()).collect(Collectors.toList())) {
            if (isFirst) {
                isFirst = false;
            } else {
                b.append(" AND");
            }
            b.append(" NOT");
            b.append(" ").append(p.getPredicate());
        }
        if (predicates.stream().anyMatch(p -> !p.isIsNot())) {
            if (!isFirst) {
                b.append(" AND");
            }
            isFirst = true;
            b.append(" (");
            for (Predicate<?> p : predicates.stream().filter(p -> !p.isIsNot()).collect(Collectors.toList())) {
                if (isFirst) {
                    isFirst = false;
                } else {
                    b.append(" OR");
                }
                b.append(" ").append(p.getPredicate());
            }
        }
        return b.append(")").toString();
    }

    @Override
    public int setParameter(Query query, int position) {
        for (Predicate<?> p : predicates.stream().filter(p -> p.isIsNot()).collect(Collectors.toList())) {
            position = p.setParameter(query, position);
        }
        for (Predicate<?> p : predicates.stream().filter(p -> !p.isIsNot()).collect(Collectors.toList())) {
            position = p.setParameter(query, position);
        }
        return position;
    }

    @Override
    public boolean isEmpty() {
        return empty;
    }

    @Override
    public int print(StringBuilder result, int position) {
        for (Predicate<?> p : predicates.stream().filter(p -> p.isIsNot()).collect(Collectors.toList())) {
            position = p.print(result, position);
        }
        for (Predicate<?> p : predicates.stream().filter(p -> !p.isIsNot()).collect(Collectors.toList())) {
            position = p.print(result, position);
        }
        return position;
    }

    private List<Predicate> parse(String columnName, String value) throws ConditionParseException {
        List<Predicate> result = new ArrayList<>();
        boolean isNot = false;
        for (String con : split(value)) {
            if (con.isEmpty()) {
                throw new ConditionParseException("Leerwerte für die Suche nicht erlaubt. " + columnName);
            }
            isNot = con.startsWith("-");
            if (isNot) {
                con = con.substring(1);
            }
            Predicate<T> p = getPredicate(columnName, con);
            p.setIsNot(isNot);
            result.add(p);
        }
        return result;
    }

    private List<String> split(String value) throws ConditionParseException {
        List<String> result = new ArrayList<>();
        if (value == null || value.length() == 0) {
            return result;
        }
        value = value.trim();
        if (value.endsWith("-")) {
            value = value.substring(0, value.length() - 1);
            value = value.trim();
        }
        if (value.length() == 0) {
            return result;
        }
        char[] cs = value.toCharArray();
        char[] temp = new char[cs.length];
        int position = 0;
        char before = cs[0];
        for (int i = 1; i < cs.length; i++) {
            if (cs[i] == ',') {
                if (before == ICondition.ESCAPE_CHARACTER) {
                    before = ',';
                } else {
                    temp[position++] = before;
                    result.add(new String(temp, 0, position).trim());
                    position = 0;
                    before = 0;
                }
            } else {
                temp[position++] = before;
                before = cs[i];
            }
        }
        
        temp[position++] = before;
        result.add(new String(temp, 0, position).trim());
            
        return result;
    }

    private Predicate<T> getPredicate(String columnName, String value) throws ConditionParseException {
        char[] cs = value.toCharArray();
        if (cs.length == 0) {
            return newSimplePredicate(columnName, value);
        }
        if (cs.length == 1 && cs[0] == '#') {
            return new EmptyPredicate<>(columnName);
        }
        char before = cs[0];
        for (int i = 1; i < cs.length; i++) {
            if (before == ICondition.ESCAPE_CHARACTER) {
                if (cs[i] == '?' || cs[i] == '*') {
                    return newLikePredicate(columnName, value);
                }
                if (cs[i] == '.' && i < cs.length - 1 && cs[i + 1] == '.') {
                    return newRangePredicate(columnName, value);
                }
            }
            before = cs[i];
        }
        return newSimplePredicate(columnName, value);
    }

    protected Predicate<T> newSimplePredicate(String columnName, String value) throws ConditionParseException {
        if (value.length() > 1) {
            switch(value.charAt(0)) {
                case '>':
                    if (value.charAt(1) == '=') {
                        return new SimplePredicate<>(columnName, newT(escapeValue(value.substring(2)),columnName, clazz), Operator.GreaterEquals);
                    } else {
                        return new SimplePredicate<>(columnName, newT(escapeValue(value.substring(1)),columnName, clazz), Operator.Greater);
                    }
                case '<':
                    if (value.charAt(1) == '=') {
                        return new SimplePredicate<>(columnName, newT(escapeValue(value.substring(2)),columnName, clazz), Operator.LessEquals);
                    } else {
                        return new SimplePredicate<>(columnName, newT(escapeValue(value.substring(1)),columnName, clazz), Operator.Less);
                    }
            }
        }
        return new SimplePredicate<>(columnName, newT(escapeValue(value),columnName, clazz), Operator.Equals);
    }

    protected Predicate newLikePredicate(String columnName, String value) {
        return new LikePredicate(columnName, value);
    }

    private Predicate<T> newRangePredicate(String columnName, String value) throws ConditionParseException {
        int x = value.indexOf("..");
        return newRangePredicate(columnName, value, x);
    }

    private Predicate<T> newRangePredicate(String columnName, String value, int x) throws ConditionParseException {
        if (x >= value.length() - 2) {
            throw new ConditionParseException("Eine Range-Condition besteht aus exakt 2 Werte, die durch das Zeichen .. getrennt sind. " + value);
        }
        return new RangePredicate<>(columnName, 
            newT(escapeValue(value.substring(0, x)), columnName, clazz),
            newT(escapeValue(value.substring(x + 2)), columnName, clazz));
    }

    private <T> T newT(String value, String columnName, Class<T> clazz) throws ConditionParseException {
        try {
            T v = clazz.getDeclaredConstructor(String.class).newInstance(value);
            return v;
        } catch(InstantiationException | NoSuchMethodException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            throw new ConditionParseException(String.format("Der Wert [%] ist nicht Typ-Kompatibel zur Spalte [%]", value, columnName));
        }
    }
    
    private String escapeValue(String value) {
        char[] cs = value.toCharArray();
        char[] temp = new char[cs.length];
        int cur = 0;
        for (int i = 0; i < cs.length; i++) {
            if (cs[i] == ICondition.ESCAPE_CHARACTER) {
                continue;
            }
            temp[cur++] = cs[i];
        }
        return new String(temp, 0, cur);
    }
}

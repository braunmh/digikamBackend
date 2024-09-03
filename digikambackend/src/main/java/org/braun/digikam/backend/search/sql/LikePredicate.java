package org.braun.digikam.backend.search.sql;

import jakarta.persistence.Query;

/**
 *
 * @author mbraun
 */
public class LikePredicate extends SimplePredicate<String> {
    
    private boolean isEscaped = false;
    
    public LikePredicate(String columnName, String value) {
        super();
        this.columnName = columnName;
        setValue(value);
    }

    @Override
    public String getPredicate() {
        return columnName + ((isEscaped) ? " like ? ESCAPE '\\'" : " like ?");
    }

    @Override
    public int setParameter(Query query, int position) {
        query.setParameter(++position, getValue());
        return position;
    }

    @Override
    public final void setValue(String value) {
        char[] temp = new char[value.length() * 2];
        int cur = 0;
        char before = 0;
        for (char c : value.toCharArray()) {
            switch(c) {
            case '%':
            case '_':
                temp[c++] = '\\';
                temp[c++] = c;
                break;
            case ICondition.ESCAPE_CHARACTER:
                if (before == ICondition.ESCAPE_CHARACTER) {
                    temp[cur++] = ICondition.ESCAPE_CHARACTER;
                } else {
                    before = ICondition.ESCAPE_CHARACTER;
                }
                break;
            case '*':
                if (before == ICondition.ESCAPE_CHARACTER) {
                    temp[cur++] = '*';
                } else {
                    temp[cur++] = '%';
                }
                break;
            case '?':
                if (before == ICondition.ESCAPE_CHARACTER) {
                    temp[cur++] = '?';
                } else {
                    temp[cur++] = '_';
                }
                break;
            default:
                temp[cur++] = c;
                before = c;
            }
        }
        super.setValue(new String(temp, 0, cur));
    }
    
    
}

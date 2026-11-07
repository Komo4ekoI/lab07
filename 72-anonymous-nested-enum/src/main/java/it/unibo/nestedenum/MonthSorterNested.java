package it.unibo.nestedenum;

import java.io.Serializable;
import java.util.Comparator;
import java.util.Locale;
import java.util.Objects;

/**
 * Implementation of {@link MonthSorter}.
 */
public final class MonthSorterNested implements MonthSorter {

    /**
     * Init a month sorter.
     */
    MonthSorterNested() {
        // No initialization required.
    }

    @Override
    public Comparator<String> sortByDays() {
        return new SortByDate();
    }

    @Override
    public Comparator<String> sortByOrder() {
        return new SortByMonthOrder();
    }

    private static Month monthValidate(final String monthName) {
        return Month.fromString(monthName);
    }

    private enum Month {
        JANUARY(31),
        FEBRUARY(29),
        MARCH(31),
        APRIL(30),
        MAY(31),
        JUNE(30),
        JULY(31),
        AUGUST(31),
        SEPTEMBER(30),
        OCTOBER(31),
        NOVEMBER(30),
        DECEMBER(31);

        private static final Locale LOCALE = Locale.ROOT;
        private final int daysCount;

        Month(final int daysCount) {
            this.daysCount = daysCount;
        }

        private static Month fromString(final String monthName) {
            Objects.requireNonNull(monthName);
            final String formatedName = monthName.toUpperCase(LOCALE);
            try {
                // passes if the name is axatly identical and UPPED
                return Month.valueOf(formatedName);
            } catch (final IllegalArgumentException e) {
                Month result = null;
                for (final Month month : values()) {
                    if (month.name().toUpperCase(LOCALE).startsWith(formatedName)) {
                        if (result != null) {
                            throw new IllegalArgumentException(
                                "More than one matche found: " + result.name() + ", " + month.name(),
                                e
                            );
                        }
                        result = month;
                    }
                }
                if (result != null) {
                    return result;
                }
                throw new IllegalArgumentException("No month matches the: " + formatedName, e);
            }
        }
    }

    private static final class SortByMonthOrder implements Comparator<String>, Serializable {
        private static final long serialVersionUID = 1L;

        @Override
        public int compare(final String o1, final String o2) {
            final Month first = monthValidate(o1);
            final Month second = monthValidate(o2);

            return Integer.compare(first.ordinal(), second.ordinal());
        }
    }

    private static final class SortByDate implements Comparator<String>, Serializable {
        private static final long serialVersionUID = 1L;

        @Override
        public int compare(final String o1, final String o2) {
            final Month first = monthValidate(o1);
            final Month second = monthValidate(o2);

            return Integer.compare(first.daysCount, second.daysCount);
        }
    }
}

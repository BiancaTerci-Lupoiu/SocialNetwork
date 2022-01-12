package project.lab6.utils.components;

import javafx.collections.FXCollections;
import javafx.scene.control.*;
import javafx.util.StringConverter;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import static java.time.temporal.ChronoUnit.DAYS;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;
import javafx.collections.ObservableSet;
import javafx.event.EventHandler;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import project.lab6.utils.Constants;

public class MultiDatePicker extends DatePicker
{
    private static final DateTimeFormatter DATE_FORMAT = Constants.DATE_FORMATTER;
    private final ObservableSet<LocalDate> selectedDates;

    public MultiDatePicker()
    {
        this.selectedDates = FXCollections.observableSet(new TreeSet<>());
        setUpDatePicker();
        withRangeSelectionMode();
    }

    private void withRangeSelectionMode()
    {
        EventHandler<MouseEvent> mouseClickedEventHandler = (MouseEvent clickEvent) ->
        {
            if (clickEvent.getButton() == MouseButton.PRIMARY)
            {
                if (!this.selectedDates.contains(super.getValue()))
                {
                    this.selectedDates.add(super.getValue());

                    this.selectedDates.addAll(getRangeGaps((LocalDate) this.selectedDates.toArray()[0], (LocalDate) this.selectedDates.toArray()[this.selectedDates.size() - 1]));

                } else
                {
                    this.selectedDates.remove(super.getValue());
                    this.selectedDates.removeAll(getTailEndDatesToRemove(this.selectedDates, super.getValue()));

                    super.setValue(getClosestDateInTree(new TreeSet<>(this.selectedDates), super.getValue()));

                }

            }
            super.show();
            clickEvent.consume();
        };

        super.setDayCellFactory((DatePicker param) -> new DateCell()
        {

            @Override
            public void updateItem(LocalDate item, boolean empty)
            {
                super.updateItem(item, empty);

                //...
                if (item != null && !empty)
                {
                    //...
                    addEventHandler(MouseEvent.MOUSE_CLICKED, mouseClickedEventHandler);
                } else
                {
                    //...
                    removeEventHandler(MouseEvent.MOUSE_CLICKED, mouseClickedEventHandler);
                }

                if (!selectedDates.isEmpty() && selectedDates.contains(item))
                {
                    if (Objects.equals(item, selectedDates.toArray()[0]) || Objects.equals(item, selectedDates.toArray()[selectedDates.size() - 1]))
                    {
                        setStyle("-fx-background-color: rgba(3, 169, 1, 0.7);");
                    } else
                    {
                        setStyle("-fx-background-color: rgba(3, 169, 244, 0.7);");
                    }
                } else
                {
                    setStyle(null);
                }

            }
        });
    }

    public ObservableSet<LocalDate> getSelectedDates()
    {
        return this.selectedDates;
    }

    private void setUpDatePicker()
    {
        super.setConverter(new StringConverter<>() {
            @Override
            public String toString(LocalDate date) {
                return (date == null) ? "" : DATE_FORMAT.format(date);
            }

            @Override
            public LocalDate fromString(String string) {
                return ((string == null) || string.isEmpty()) ? null : LocalDate.parse(string, DATE_FORMAT);
            }
        });

        EventHandler<MouseEvent> mouseClickedEventHandler = (MouseEvent clickEvent) ->
        {
            if (clickEvent.getButton() == MouseButton.PRIMARY)
            {
                if (!this.selectedDates.contains(super.getValue()))
                {
                    this.selectedDates.add(super.getValue());
                } else
                {
                    this.selectedDates.remove(super.getValue());

                    super.setValue(getClosestDateInTree(new TreeSet<>(this.selectedDates), super.getValue()));

                }

            }
            super.show();
            clickEvent.consume();
        };

        super.setDayCellFactory((DatePicker param) -> new DateCell()
        {
            @Override
            public void updateItem(LocalDate item, boolean empty)
            {
                super.updateItem(item, empty);

                //...
                if (item != null && !empty)
                {
                    //...
                    addEventHandler(MouseEvent.MOUSE_CLICKED, mouseClickedEventHandler);
                } else
                {
                    //...
                    removeEventHandler(MouseEvent.MOUSE_CLICKED, mouseClickedEventHandler);
                }

                if (selectedDates.contains(item))
                {

                    setStyle("-fx-background-color: rgba(3, 169, 244, 0.7);");

                } else
                {
                    setStyle(null);

                }
            }
        });

    }

    private static Set<LocalDate> getTailEndDatesToRemove(Set<LocalDate> dates, LocalDate date)
    {

        TreeSet<LocalDate> tempTree = new TreeSet<>(dates);

        tempTree.add(date);

        int higher = tempTree.tailSet(date).size();
        int lower = tempTree.headSet(date).size();

        if (lower <= higher)
        {
            return tempTree.headSet(date);
        } else {
            return tempTree.tailSet(date);
        }

    }

    private static LocalDate getClosestDateInTree(TreeSet<LocalDate> dates, LocalDate date)
    {
        Long lower = null;
        Long higher = null;

        if (dates.isEmpty())
        {
            return null;
        }

        if (dates.size() == 1)
        {
            return dates.first();
        }

        if (dates.lower(date) != null)
        {
            lower = Math.abs(DAYS.between(date, dates.lower(date)));
        }
        if (dates.higher(date) != null)
        {
            higher = Math.abs(DAYS.between(date, dates.higher(date)));
        }

        if (lower == null)
        {
            return dates.higher(date);
        } else if (higher == null)
        {
            return dates.lower(date);
        } else if (lower <= higher)
        {
            return dates.lower(date);
        } else {
            return dates.higher(date);
        }
    }

    private static Set<LocalDate> getRangeGaps(LocalDate min, LocalDate max)
    {
        Set<LocalDate> rangeGaps = new LinkedHashSet<>();

        if (min == null || max == null)
        {
            return rangeGaps;
        }

        LocalDate lastDate = min.plusDays(1);
        while (lastDate.isAfter(min) && lastDate.isBefore(max))
        {
            rangeGaps.add(lastDate);
            lastDate = lastDate.plusDays(1);

        }
        return rangeGaps;
    }
}
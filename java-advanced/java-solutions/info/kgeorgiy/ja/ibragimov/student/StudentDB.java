package info.kgeorgiy.ja.ibragimov.student;

import info.kgeorgiy.java.advanced.student.Group;
import info.kgeorgiy.java.advanced.student.GroupName;
import info.kgeorgiy.java.advanced.student.GroupQuery;
import info.kgeorgiy.java.advanced.student.Student;

import java.util.*;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class StudentDB implements GroupQuery {
    private static final Comparator<Student> STUDENT_NAME_COMPARATOR = Comparator
            .comparing(Student::getLastName)
            .thenComparing(Student::getFirstName)
            .reversed()
            .thenComparing(Student::getId);

    @Override
    public List<String> getFirstNames(final List<Student> students) {
        return getFromStudents(students, Student::getFirstName);
    }

    @Override
    public List<String> getLastNames(final List<Student> students) {
        return getFromStudents(students, Student::getLastName);
    }

    @Override
    public List<GroupName> getGroups(final List<Student> students) {
        return getFromStudents(students, Student::getGroup);
    }

    @Override
    public List<String> getFullNames(final List<Student> students) {
        return getFromStudents(students, student -> student.getFirstName() + " " + student.getLastName());
    }

    @Override
    public Set<String> getDistinctFirstNames(final List<Student> students) {
        return students.stream()
                .map(Student::getFirstName)
                .sorted()
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    @Override
    public String getMaxStudentFirstName(final List<Student> students) {
        return students.stream()
                .max(Student::compareTo)
                .map(Student::getFirstName)
                .orElse("");
    }

    @Override
    public List<Student> sortStudentsById(final Collection<Student> students) {
        return sortStudentsBy(students, Student::compareTo);
    }

    @Override
    public List<Student> sortStudentsByName(final Collection<Student> students) {
        return sortStudentsBy(students, STUDENT_NAME_COMPARATOR);
    }

    @Override
    public List<Student> findStudentsByFirstName(final Collection<Student> students, final String name) {
        return findStudentsBy(students, Student::getFirstName, name);
    }

    @Override
    public List<Student> findStudentsByLastName(final Collection<Student> students, final String name) {
        return findStudentsBy(students, Student::getLastName, name);
    }

    @Override
    public List<Student> findStudentsByGroup(final Collection<Student> students, final GroupName group) {
        return findStudentsBy(students, Student::getGroup, group);
    }

    @Override
    public Map<String, String> findStudentNamesByGroup(final Collection<Student> students, final GroupName group) {
        return findStudentsByGroup(students, group).stream().collect(Collectors.toUnmodifiableMap(
                Student::getLastName,
                Student::getFirstName,
                BinaryOperator.minBy(Comparator.naturalOrder())
        ));
    }

    @Override
    public List<Group> getGroupsByName(final Collection<Student> students) {
        return getGroupsBy(students, STUDENT_NAME_COMPARATOR);
    }

    @Override
    public List<Group> getGroupsById(final Collection<Student> students) {
        return getGroupsBy(students, Student::compareTo);
    }

    @Override
    public GroupName getLargestGroup(final Collection<Student> students) {
        return getLargestGroupsBy(students, Collectors.summingInt(e -> 1), Comparator.naturalOrder());
    }

    @Override
    public GroupName getLargestGroupFirstName(final Collection<Student> students) {
        return getLargestGroupsBy(
                students,
                Collectors.collectingAndThen(
                        Collectors.mapping(Student::getFirstName, Collectors.toCollection(HashSet::new)),
                        HashSet::size
                ),
                Comparator.reverseOrder()
        );
    }

    private static <T> List<T> getFromStudents(final List<Student> students,
                                               final Function<? super Student, T> function) {
        return students.stream().map(function).collect(Collectors.toList());
    }

    private List<Student> sortStudentsBy(final Collection<Student> students,
                                         final Comparator<? super Student> comparator) {
        return students.stream().sorted(comparator).collect(Collectors.toList());
    }

    private <T> List<Student> findStudentsBy(final Collection<Student> students,
                                             final Function<? super Student, T> function,
                                             final T parameter) {
        return students.stream()
                .filter(student -> function.apply(student).equals(parameter))
                .sorted(STUDENT_NAME_COMPARATOR)
                .collect(Collectors.toList());
    }

    private List<Group> getGroupsBy(final Collection<Student> students,
                                    final Comparator<? super Student> comparator) {
        return students.stream()
                .collect(Collectors.groupingBy(Student::getGroup, Collectors.toList()))
                .entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .map(entry -> new Group(
                        entry.getKey(),
                        entry.getValue().stream().sorted(comparator).collect(Collectors.toList())))
                .collect(Collectors.toList());
    }

    private GroupName getLargestGroupsBy(final Collection<Student> students,
                                         final Collector<Student, ?, Integer> collector,
                                         final Comparator<GroupName> groupNameComparator) {
        return students.stream()
                .collect(Collectors.groupingBy(Student::getGroup, collector))
                .entrySet().stream()
                .max(Map.Entry.<GroupName, Integer>comparingByValue()
                        .thenComparing(Map.Entry.comparingByKey(groupNameComparator)))
                .map(Map.Entry::getKey)
                .orElse(null);
    }
}

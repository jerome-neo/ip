import java.io.IOException;
import java.util.Scanner;
import java.util.ArrayList;


public class Duke {

    /**
     * Prints the stored Tasks.
     * @param stored
     */
    public static void printList(ArrayList<Task> stored) {
        System.out.println("Here are the tasks in your list: ");
        for (int i = 0; i < stored.size(); i++) {
            Task task = stored.get(i);
            System.out.println((i+1) + ". " + task.toString());
        }
    }

    public static void main(String[] args) throws IOException {
        String logo = " ____        _        \n"
                + "|  _ \\ _   _| | _____ \n"
                + "| | | | | | | |/ / _ \\\n"
                + "| |_| | |_| |   <  __/\n"
                + "|____/ \\__,_|_|\\_\\___|\n";
        String separator = "____________________________________________________________";
        System.out.println("Hello from\n" + logo);
        System.out.println(separator);
        System.out.println("Hello! I'm Duke");
        System.out.println("What can I do for you?");
        System.out.println(separator);

        Scanner scanner = new Scanner(System.in);
        ArrayList<Task> myTasks = new ArrayList<>();
        String input;
        while(true) {
            input = scanner.nextLine();
            if (input.equals("bye")) {
                System.out.println("Bye. Hope to see you again soon!");
                System.out.println(separator);
                break;
            } else if (input.equals("list")) {
                printList(myTasks);
                System.out.println(separator);
            } else if (input.startsWith("mark")) {
                try {
                    String pattern = "mark \\d*";
                    boolean isMatch = input.matches(pattern);
                    if (!isMatch){
                        throw new DukeException("☹ OOPS!!! The description of a mark cannot be empty.");
                    }
                } catch (DukeException e) {
                    System.out.println(e.getMessage());
                }
                int taskNumber = Integer.parseInt(input.substring(5));
                taskNumber--;

                try {
                    if (taskNumber < 0 || taskNumber >= myTasks.size()){
                        throw new DukeException("☹ OOPS!!! The number to mark is invalid.");
                    }
                } catch (DukeException e) {
                    System.out.println(e.getMessage());
                }
                Task task = myTasks.get(taskNumber);
                task.taskDone();
            } else if (input.startsWith("unmark")) {
                try {
                    String pattern = "unmark \\d*";
                    boolean isMatch = input.matches(pattern);
                    if (!isMatch){
                        throw new DukeException("☹ OOPS!!! The description of a unmark cannot be empty.");
                    }
                } catch (DukeException e) {
                    System.out.println(e.getMessage());
                }
                int taskNumber = Integer.parseInt(input.substring(7));
                taskNumber--;
                try {
                    if (taskNumber < 0 || taskNumber >= myTasks.size()){
                        throw new DukeException("☹ OOPS!!! The number to unmark is invalid.");
                    }
                } catch (DukeException e) {
                    System.out.println(e.getMessage());
                }
                Task task = myTasks.get(taskNumber);
                task.taskNotDone();
            } else if (input.startsWith("todo")) {
                try {
                    String pattern = "todo\\s+(.*)\\s+";
                    boolean isMatch = input.matches(pattern);
                    if (!isMatch){
                        throw new DukeException("☹ OOPS!!! The description of a todo cannot be empty.");
                    }
                } catch (DukeException e) {
                    System.out.println(e.getMessage());
                }
                String taskDescription = input.substring(5);
                Todo task = new Todo(taskDescription);
                myTasks.add(task);
                task.announceAdded(myTasks);
            } else if (input.startsWith("deadline")) {
                try {
                    String pattern = "deadline\\s+(.*)\\s+/by\\s+(.*)";
                    boolean isMatch = input.matches(pattern);
                    if (!isMatch){
                        throw new DukeException("☹ OOPS!!! The description of a deadline cannot be empty.");
                    }
                } catch (DukeException e) {
                    System.out.println(e.getMessage());
                }
                String[] temp = input.substring(9).split(" /by ");
                String taskDescription = temp[0];
                String by = temp[1];
                Deadline task = new Deadline(taskDescription, by);
                myTasks.add(task);
                task.announceAdded(myTasks);
            } else if (input.startsWith("event")) {
                try {
                    String pattern = "event\\s+(.*)\\s+/from\\s+(.*)\\s+/to\\s+(.*)";
                    boolean isMatch = input.matches(pattern);
                    if (!isMatch){
                        throw new DukeException("☹ OOPS!!! The description of a event cannot be empty.");
                    }
                } catch (DukeException e) {
                    System.out.println(e.getMessage());
                }
                String[] temp = input.substring(6).split(" /from ");
                String taskDescription = temp[0];
                String[] time = temp[1].split(" /to ");
                String from = time[0];
                String to = time[1];
                Event task = new Event(taskDescription, from, to);
                myTasks.add(task);
                task.announceAdded(myTasks);
            } else if (input.startsWith("delete")) {
                try {
                    String pattern = "delete\\s+\\d*";
                    boolean isMatch = input.matches(pattern);
                    if (!isMatch){
                        throw new DukeException("☹ OOPS!!! The description of a delete cannot be empty.");
                    }
                } catch (DukeException e) {
                    System.out.println(e.getMessage());
                }

                int taskNumber = Integer.parseInt(input.substring(7));
                taskNumber--;

                try {
                    if (taskNumber < 0 || taskNumber >= myTasks.size()){
                        throw new DukeException("☹ OOPS!!! The number to delete is invalid.");
                    }
                } catch (DukeException e) {
                    System.out.println(e.getMessage());
                }
                Task task = myTasks.get(taskNumber);
                myTasks.remove(task);
                task.announceRemoved(myTasks);
            } else {
                System.out.println("Invalid response. Please try: todo, deadline or event. :)");
            }
            // Auto-save state in file
            Save.autoSave(myTasks);
        }
    }
}


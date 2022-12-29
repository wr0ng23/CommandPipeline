import java.util.*;

public class CommandPipeline {
    private int allCountOfTics = 0; // общее количество тиков для конвейрной работы
    private final int countOfCommands; // всего количество команд для выполнения
    private int countOfCommandsNow = 1; // количество команд выложенных на конвейер
    private final Command[] commands; // Изначально массив команд для выполнения
    private ArrayList<Command> queue = new ArrayList<>(); // очередь для команд, которые должны выполнить свои стадии,
    // чтобы передвинуть конвейер дальше
    private ArrayList<Command> queueForWritingInMemoryCommands = new ArrayList<>(); // очередь для команд, которые
    // пишут в память на данной стадии

    public CommandPipeline(int countOfCommands) {
        this.countOfCommands = countOfCommands;
        commands = new Command[countOfCommands];

        for (int i = 0; i < countOfCommands; ++i) {
            commands[i] = new Command();
        }
    }

    public void printTicsForAllCommandsPipelineEx() {
        System.out.println("Конвейреная обработка команд");
        for (int i = 0; i < countOfCommands; ++i) {
            System.out.println("Команда: " + (i + 1) + " - " + commands[i].getCountOfClockCyclesForPipelineEx());
        }
    }

    public void printTicsForAllCommandsSeqEx() {
        System.out.println("Последовательная обработка команд");
        for (int i = 0; i < countOfCommands; ++i) {
            System.out.println("Команда: " + (i + 1) + " - " + commands[i].getCountOfClockCyclesForSeqEx());
        }
    }

    public int getAllSeqTics() {
        int seqTics = 0;
        for (var c : commands) {
            seqTics += c.getCountOfClockCyclesForSeqEx();
        }
        return seqTics;
    }

    public double getAvgSeqTics() {
        return (double) getAllSeqTics() / (double) countOfCommands;
    }

    public int getAllPipelineOfTics() {
        return allCountOfTics;
    }

    public double getAvgPipelineTics() {
        return (double) allCountOfTics / (double) countOfCommands;
    }

    public void startThePipeline() {
        while (!commands[countOfCommands - 1].isFinished()) { // выполнять, пока не будет завершена последняя команда
            createQueuesOfCurrentCommands(); // формируем очереди команд на выполнение на текущей стадии конвейера
            executeCommandsAtTheCurrentStage(); // выполняем все команды на текущей стадии конвейра, считаем кол-во тиков

            if (countOfCommands != countOfCommandsNow) // добавляем новую команду на выполнение, пока не превышено
                countOfCommandsNow++;                 // заданное число команд

            goToNextStageForCurrentCommands(); // переходим к следующей стадии конвейера
            unsetCommandsWaiting(); // убираем ождиание команд для записи и чтения в память для следующего шага
            queue = new ArrayList<>(); // очищаем очереди
            queueForWritingInMemoryCommands = new ArrayList<>();
        }
    }

    private void executeCommandsAtTheCurrentStage() {
        while (!checkForPromotion()) {
            Command c = choiceOfCommandForWritingInMemory();
            performActionsForCommands();
            checkForNewCommandForWritingInMemory(c);
            allCountOfTics++;
        }
    }

    private void checkForNewCommandForWritingInMemory(Command c) {
        if (c.isCanGoToAnotherStage()) {
            c.setWaiting();
            queueForWritingInMemoryCommands.remove(0);
        }
    }

    private Command choiceOfCommandForWritingInMemory() {
        Command c = new Command();
        if (!queueForWritingInMemoryCommands.isEmpty()) {
            c = queueForWritingInMemoryCommands.get(0);
            setCommandsWaiting();
            c.unsetWaiting();
        }
        return c;
    }

    private void createQueuesOfCurrentCommands() {
        for (int i = 0; i < countOfCommandsNow; ++i) {
            if (!commands[i].isFinished()) {
                if (commands[i].isWritesToMemory()) {
                    queueForWritingInMemoryCommands.add(commands[i]);
                }
                queue.add(commands[i]);
            }
        }
    }

    private void performActionsForCommands() {
        for (var com : queue) {
            com.choiceOfActionToPerformOnThisStep();
        }
    }

    private void unsetCommandsWaiting() {
        for (var com : queue) {
            com.unsetWaiting();
        }
    }

    private void setCommandsWaiting() {
        for (var c : queueForWritingInMemoryCommands) {
            c.setWaiting();
        }
    }

    private boolean checkForPromotion() {
        for (var c : queue) {
            if (!c.isCanGoToAnotherStage()) return false;
        }
        return true;
    }

    private void goToNextStageForCurrentCommands() {
        for (var c : queue) {
            c.goToTheNextStage();
        }
    }
}

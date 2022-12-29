public class Command {
    private int countOfClockCyclesForSeqEx = 0; // счетчик тиков при последовательном выполнение
    private int countOfClockCyclesForPipelineEx = 0; // счетчик тиков при конвейрном выполнение
    private double randomNumber = 0.0; // случайное число для первого операнда и выбора типа команды
    private double randomNumberForOperand2 = 0.0; // случайное число для второго операнда и записи в память
    private int counter = 0; // счетчик прошедних тактов для операндов или типа команды, если используется N или M
    private int currentStatusOfCommand = 1; // стадий - 5, счетчик текущей стадии
    private boolean canGoToAnotherStage = false; // можно перейти к следующей стадии
    private boolean writesToMemory = false; // команда пишется в память
    private boolean isWaiting = false; // команда стоит в очереди на запись
    private boolean isFinished = false; // команда заврешена

    public void choiceOfActionToPerformOnThisStep() {
        if (isFinished) return;

        if (canGoToAnotherStage || isWaiting) {
            countOfClockCyclesForPipelineEx++;
            return;
        }

        switch (currentStatusOfCommand) {
            case 1 -> decrypt();

            case 2 -> selectFirstOperand();

            case 3 -> selectSecondOperand();

            case 4 -> selectCommand();

            case 5 -> writeToMemory();

            default -> System.out.println("Команда была записана в память!");
        }
    }

    public boolean isFinished() {
        return isFinished;
    }

    public void setWaiting() {
        isWaiting = true;
    }

    public void unsetWaiting() {
        isWaiting = false;
    }

    public boolean isCanGoToAnotherStage() {
        return canGoToAnotherStage;
    }

    public boolean isWritesToMemory() {
        return writesToMemory;
    }

    public int getCountOfClockCyclesForSeqEx() {
        return countOfClockCyclesForSeqEx;
    }

    public int getCountOfClockCyclesForPipelineEx() {
        return countOfClockCyclesForPipelineEx;
    }

    public void goToTheNextStage() {
        canGoToAnotherStage = false;
        currentStatusOfCommand++;
        writesToMemory = currentStatusOfCommand == 2 || currentStatusOfCommand == 3 || currentStatusOfCommand == 5;
    }

    private void decrypt() {
        countOfClockCyclesForSeqEx++;
        countOfClockCyclesForPipelineEx++;
        canGoToAnotherStage = true;
    }

    private void selectFirstOperand() {
        double randomNumber = generateRandomNumber();
        selectOperandOrCommand(Parameters.P1, Parameters.N, randomNumber);
    }

    private void selectSecondOperand() {
        randomNumberForOperand2 = generateRandomNumber();
        selectOperandOrCommand(Parameters.P1, Parameters.N, randomNumberForOperand2);
    }

    private void selectCommand() {
        double randomNumber = generateRandomNumber();
        selectOperandOrCommand(Parameters.P2, Parameters.M, randomNumber);
    }

    private void writeToMemory() {
        selectOperandOrCommand(Parameters.P1, Parameters.N, randomNumberForOperand2);
        if (canGoToAnotherStage) isFinished = true;
    }

    private double generateRandomNumber() {
        if (counter == 0) { // генерируем случайное число и проверяем не было ли оно уже создано
            randomNumber = Math.random();
        }
        return randomNumber;
    }

    private void selectOperandOrCommand(double probability, int numberOfClockCycles, double randomNumber) {
        if (randomNumber > probability) { // если число больше заданной вероятности, выбор операндов или вычисление
            counter++;                   // результата будет выполняться N или M тактов
            if (counter == numberOfClockCycles) { // проверка не прошло ли нужное количество тактов для совершения
                counter = 0;                     // перехода к следуещему этапу
                canGoToAnotherStage = true;
            }
        } else {
            canGoToAnotherStage = true;
        }

        countOfClockCyclesForSeqEx++;
        countOfClockCyclesForPipelineEx++;
    }
}

public class Main {
    private final static int countOfCommands = 200;

    public static void main(String[] args) {
        commandPipeline pipeline = new commandPipeline(countOfCommands);
        pipeline.startThePipeline();

        System.out.println("Для конвейрной работы общее время выполнения всех команд: "
                + pipeline.getAllPipelineOfTics());
        System.out.println("Для конвейрной работы среднее время выполнение одной команды: "
                + pipeline.getAvgPipelineTics());
        pipeline.printTicsForAllCommandsPipelineEx();

        System.out.println("\nДля последовательной работы общее время выполнения всех команд: "
                + pipeline.getAllSeqTics());
        System.out.println("Для последовательной работы среднее время выполнение одной команды: "
                + pipeline.getAvgSeqTics());
        pipeline.printTicsForAllCommandsSeqEx();


        String result =
                String.format("%.3f", (double)pipeline.getAllSeqTics() / (double) pipeline.getAllPipelineOfTics());
        System.out.println("\nКонвейер дает выйгрыш в "
                + result + " раз");
    }
}
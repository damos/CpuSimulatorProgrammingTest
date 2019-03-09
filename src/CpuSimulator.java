import com.sun.istack.internal.NotNull;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CpuSimulator {
    private static final String TO_STRING = "A:%d, B:%d";
    private byte registers[] = new byte[Register.values().length];
    private byte memory[] = new byte[256];

    public enum Register{
        A, B;
    }

    @FunctionalInterface
    public interface TriConsumer<T, U, V> {
        void accept(T t, U u, V v);
    }

    public enum Instruction {
        SET((cpu, register, _byte) -> {
            cpu.registers[register.ordinal()] = _byte;
        }),
        ADD((cpu, register, _byte) -> {
            cpu.registers[register.ordinal()] += _byte;
        }),
        SUBT((cpu, register, _byte) -> {
            cpu.registers[register.ordinal()] -= _byte;
        }),
        LSHIFT((cpu, register, _byte) -> {
            cpu.registers[register.ordinal()] <<= _byte;
        }),
        READ((cpu, register, _byte) -> {
            cpu.registers[register.ordinal()] = cpu.memory[_byte & 0xFF];
        }),
        WRITE((cpu, register, _byte) -> {
            cpu.memory[_byte & 0xFF] = cpu.registers[register.ordinal()];
        });

        private TriConsumer<CpuSimulator, Register, Byte> operation;
        Instruction(TriConsumer<CpuSimulator, Register, Byte> operation) {
            this.operation = operation;
        }
    }

    private void execute(@NotNull Command command){
        if(command.inst == null || command.required == null) return;

        if(command.optional == null) {
            command.inst.operation.accept(this, command.required, command._byte);
        }
        else{
            command.inst.operation.accept(this, command.required, this.registers[command.optional.ordinal()]);
        }
    }

    public String toString(){
        return String.format(TO_STRING, this.registers[0] & 0xFF, this.registers[1] & 0xFF);
    }

    private void print(){
        System.out.println(this.toString());
    }

    private static class Command{
        private static final String INVALID_COMMAND = "Invalid command: %s";
        private static final Pattern pattern = Pattern.compile("([A-Z]*) ([A-Z]),([A-Z]|[0-9]*)");
        private Instruction inst;
        private Register required, optional;
        private byte _byte;

        private Command read(@NotNull String commandLine){
            //if it's empty skip.
            if(commandLine.trim().isEmpty()){
                this.inst = null;
                this.required = null;
                this.optional = null;
                return this;
            }

            Matcher matcher = pattern.matcher(commandLine);
            if(matcher.matches()){
                try{
                    this.inst = Instruction.valueOf(matcher.group(1));
                    this.required = Register.valueOf(matcher.group(2));
                    String finalArg = matcher.group(3);
                    try {
                        this.optional = Register.valueOf(finalArg);
                    } catch (IllegalArgumentException e) {
                        this._byte = Integer.valueOf(finalArg).byteValue();
                    }
                } catch (IllegalArgumentException e) {
                    throw new RuntimeException(String.format(INVALID_COMMAND, commandLine));
                }
            }
            else {
                throw new RuntimeException(String.format(INVALID_COMMAND, commandLine));
            }
            return this;
        }
    }

    public static void main(String [] args) throws IOException{
        if(args.length == 0){
            System.out.println("No command file passed as an argument.");
        }
        else{
            final CpuSimulator cpu = new CpuSimulator();
            final Command container = new Command();

            for(String arg : args) {
                //Remember this is lazy, forEach is called before the next execution of map.
                Files.lines(Paths.get(arg))
                        .map(container::read)
                        .forEach(cpu::execute);
            }
            cpu.print();

        }
    }
}

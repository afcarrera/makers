package ec.com.carrera.makers.loan.handler;

public interface Handler<T> {
    void setNextHandler(Handler<T> nextHandler);

    void process(T context);

    void checkNextHandler(T context);
}

package ec.com.carrera.makers.loan.handler;

public abstract class AbstractHandler <C> implements Handler<C> {
    private Handler<C> nextHandler;

    public AbstractHandler() {
    }

    public void checkNextHandler(C context) {
        if (this.nextHandler != null) {
            this.nextHandler.process(context);
        }

    }

    public void setNextHandler(Handler<C> nextHandler) {
        this.nextHandler = nextHandler;
    }
}

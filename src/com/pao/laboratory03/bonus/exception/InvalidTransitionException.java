package com.pao.laboratory03.bonus.exception;
import com.pao.laboratory03.bonus.model.Status;

public class InvalidTransitionException extends RuntimeException {
    public InvalidTransitionException(Status from, Status to){
        super("Nu se poate trece din " + from + " in " + to);
    }
}
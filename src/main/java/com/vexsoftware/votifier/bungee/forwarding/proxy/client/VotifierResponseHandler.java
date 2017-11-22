package com.vexsoftware.votifier.bungee.forwarding.proxy.client;

public abstract interface VotifierResponseHandler {

    public abstract void onSuccess();

    public abstract void onFailure(Throwable paramThrowable);
}

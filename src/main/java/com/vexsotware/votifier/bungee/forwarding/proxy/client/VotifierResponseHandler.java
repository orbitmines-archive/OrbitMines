package com.vexsotware.votifier.bungee.forwarding.proxy.client;

public abstract interface VotifierResponseHandler {

    public abstract void onSuccess();

    public abstract void onFailure(Throwable paramThrowable);
}

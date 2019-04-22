package net.chinacloud.mediator.kaola.init;

import net.chinacloud.mediator.kaola.exception.KaoLaException;
import net.chinacloud.mediator.kaola.request.KaoLaRequest;


public interface KaoLaClient {

	public abstract Object execute(KaoLaRequest request) throws KaoLaException;
}

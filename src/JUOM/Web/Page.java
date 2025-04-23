package JUOM.Web;

import JUOM.JHTML.JHTML;

public abstract class Page extends ServerObject {




    protected abstract JHTML startingPage();



    @Override
    protected void handleURL(Client c, URL url) throws CompleteClientResponse {

        if(url.path().equals("/")) {
            c.setResponse(startingPage()).completeResponse();
        } else {
            super.handleURL(c, url);
        }
    }


}

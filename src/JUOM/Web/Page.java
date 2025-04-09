package JUOM.Web;

import JUOM.JHTML.JHTML;

public abstract class Page extends ServerObject {




    protected abstract JHTML startingPage();



    @Override
    protected void handleURL(Client c, String url) throws CompleteClientResponse {

        if(url.equals("/")) {
            c.setResponse(startingPage());
        } else {
            super.handleURL(c, url);
        }
    }


}

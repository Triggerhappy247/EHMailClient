import com.jacob.activeX.ActiveXComponent;
import com.jacob.com.Dispatch;
import com.jacob.com.Variant;

public class hmailServer {
    public static void main(String[] args)
    {
        ActiveXComponent application = new ActiveXComponent("hMailServer.Application");
        System.out.println("hMailServer Library Loaded");
        Dispatch.call(application,"Authenticate","Administrator","123456");
        Variant variant = application.getProperty("Domains");
        Dispatch domains = variant.getDispatch();
        variant = Dispatch.call(domains,"ItemByName","mail.localserver.com");
        Dispatch domain = variant.getDispatch();
        variant = Dispatch.get(domain,"Accounts");
        Dispatch accounts = variant.getDispatch();
        variant = Dispatch.call(accounts,"Add");
        Dispatch account = variant.getDispatch();
        Dispatch.put(account,"Address","bhatia@mail.localserver.com");
        Dispatch.put(account,"Password","secret");
        boolean t = true;
        Dispatch.put(account,"Active",t);
        Dispatch.put(account,"MaxSize",100);
        Dispatch.call(account,"Save");
    }

}

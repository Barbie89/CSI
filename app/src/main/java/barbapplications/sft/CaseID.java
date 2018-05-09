package barbapplications.sft;

import java.io.Serializable;

public class CaseID implements Serializable{
    private int cid;
    CaseID(int cid){
        this.cid = cid;
    }

    public int getCid() {
        return cid;
    }
}

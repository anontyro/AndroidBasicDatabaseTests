package com.tradexc.basicdatabase;

/**
 * Created by Spreetrip on 12/15/2016.
 */

public class ItemGroupItem {
    public String itemgroupcode, itemgroupname, itemgroupnum,itemgroupstatus, companynum;

    public ItemGroupItem(String itemgroupcode, String itemgroupname, String itemgroupnum,
                         String itemgroupstatus, String companynum) {
        this.itemgroupcode = itemgroupcode;
        this.itemgroupname = itemgroupname;
        this.itemgroupnum = itemgroupnum;
        this.itemgroupstatus = itemgroupstatus;
        this.companynum = companynum;
    }
}

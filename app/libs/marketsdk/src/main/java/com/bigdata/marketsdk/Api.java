package com.bigdata.marketsdk;

/**
 * user:kun
 * Date:2016/10/25 or 下午1:56
 * email:hekun@gamil.com
 * Desc: 所有接口地址
 */

public class Api {

    public static final String BaseUrl = "http://q1.chinabigdata.com";


    public static final String FENSHIBASE = BaseUrl + "/quote/SerialsRequest?code=";

    public static final String FENSHI = "&indicate-name=TrendLine&addition-indicate-names=Now,Open,PrevClose,High,Low,Name,Volume,Change,ChangeRange,VolumeSpread,PE,TtlShr,FundFlowContent,NewsRatingDate,NewsRatingLevel,NewsRatingName";

    public static final String A = "http://q1.chinabigdata.com/quote/SortRequest?indicate-name=ChangeRange&sector-id=";

    public static final String RIK = "&indicate-name=KLine&number-from-begin=-80&addition-indicate-names=Now,Open,PrevClose,High,Low,Name,Volume,Change,ChangeRange&number-type=";

    public static final String MINGXI = "&indicate-name=SubDeal&number-from-begin=-10&number-type=2";

    public static final String XQ = "http://q1.chinabigdata.com/quote/ScalarRequest?code=";

    public static final String WUDANG = "&indicate-name=Now,PrevClose,BidPrice1,BidPrice2,BidPrice3,BidPrice4,BidPrice5,BidVolume1,BidVolume2,BidVolume3,BidVolume4,BidVolume5,AskPrice1,AskPrice2,AskPrice3,AskPrice4,AskPrice5,AskVolume1,AskVolume2,AskVolume3,AskVolume4,AskVolume5";

    public static final String ZIXUN = "http://t1.chinabigdata.com/PostService.aspx?Service=DataSourceService.Gets&Function=GetsService&BD_CODE=";

    public static final String YANBAO = "&atype=json&COUNT=5&INDEX=1&ObjId=";

    public static final String JXH = "&atype=json&COUNT=5&ID=0&ObjId=";

    public static final String FENSHI_03 = "&indicate-name=TrendLine&addition-indicate-names=Now,Open,PrevClsPrcFd,PrevClose,High,Low&number-from-begin=-5";

    public static final String F10 = "http://f10.chinabigdata.com/default.htm?&ql=1&istophide=1&c=";

    public static final String CSHU = "&indicate-name=PrevClose,Open,Now,High,Low,Amount,Volume,ChangeHandsRate,VolRatio,TtlShr,TtlShrNtlc,VolumeSpread,PEttm,Eps,Change,ChangeRange,Name";

    public static final String A_K = "&indicate-name=KLine&number-from-begin=-5&number-type=16&addition-indicate-names=Now,Open,PrevClose,High,Low,Name,Volume,Change,ChangeRange,VolumeSpread,PE,TtlShr";

    public static final String YB_XQ = "http://59.110.25.120:8080/DKService/PostService.aspx?Service=DataSourceService.Gets&Function=GetsService&atype=json&ObjId=1595&CONT_ID=";

    public static final String GG_XQ = "http://t1.chinabigdata.com/PostService.aspx?Service=DataSourceService.Gets&Function=GetsService&atype=json&ObjId=1589&ID=";

    public static final String STRUrl = "http://t1.chinabigdata.com/PostService.aspx?Service=NewsEventService.Gets&Function=GetsService&label=";


    public static final String MoreUrl = "http://t1.chinabigdata.com/PostService.aspx?Service=DataSourceService.Gets&Function=GetsService&ObjId=1553&atype=JSON";


}

package com.fstop.eachadmin.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fstop.eachadmin.repository.OnBlockTabRepository;
import com.fstop.eachadmin.repository.VwOnBlockTabRepository;
import com.fstop.fcore.util.StrUtils;
@Service
public class OnblocktabService {
	@Autowired
	private OnBlockTabRepository onBlockTabRepository;
	@Autowired
	private VwOnBlockTabRepository vwOnBlockTabRepository;

public Map showDetail(String txdate,String stan){
		
		Map po = null;
		Map rtnMap = new HashMap();
		String condition ="";
		try {
			txdate = StrUtils.isEmpty(txdate)?"":txdate;
			stan = StrUtils.isEmpty(stan)?"":stan;
			
			StringBuffer sql = new StringBuffer();
			sql.append("SELECT TRANSLATE('abcd-ef-gh', VARCHAR(A.TXDATE), 'abcdefgh') AS TXDATE,VARCHAR(A.STAN) AS STAN,VARCHAR_FORMAT(NEWTXDT, 'YYYY-MM-DD HH24:MI:SS') AS TXDT,A.PCODE || '-' || ETC.EACH_TXN_NAME AS PCODE_DESC, ");
//20151023 edit by hugo req by UAT-20151022-02
//			sql.append("A.SENDERBANK || '-' || (SELECT B.BGBK_NAME FROM BANK_GROUP B WHERE A.SENDERBANK = B.BGBK_ID) AS SENDERBANK_NAME, ");
//			sql.append("A.RECEIVERBANK || '-' || (SELECT B.BGBK_NAME FROM BANK_GROUP B WHERE A.RECEIVERBANK = B.BGBK_ID) AS RECEIVERBANK_NAME, ");
			sql.append("A.SENDERBANK || '-' || COALESCE((SELECT B.BRBK_NAME FROM BANK_BRANCH B WHERE A.SENDERBANK = B.BRBK_ID),'') AS SENDERBANK_NAME, ");
			sql.append("A.RECEIVERBANK || '-' || COALESCE((SELECT B.BRBK_NAME FROM BANK_BRANCH B WHERE A.RECEIVERBANK = B.BRBK_ID),'') AS RECEIVERBANK_NAME, ");
			sql.append("(SELECT B.ERR_DESC FROM TXN_ERROR_CODE B WHERE A.CONRESULTCODE = B.ERROR_ID) AS CONRESULTCODE_DESC,A.ACCTCODE, ");
			sql.append("A.SENDERCLEARING || '-' || (SELECT B.BGBK_NAME FROM BANK_GROUP B WHERE A.SENDERCLEARING = B.BGBK_ID) AS SENDERCLEARING_NAME, ");
			sql.append("A.INCLEARING || '-' || (SELECT B.BGBK_NAME FROM BANK_GROUP B WHERE A.INCLEARING = B.BGBK_ID) AS INCLEARING_NAME, ");
			sql.append("A.OUTCLEARING || '-' || (SELECT B.BGBK_NAME FROM BANK_GROUP B WHERE A.OUTCLEARING = B.BGBK_ID) AS OUTCLEARING_NAME, ");
			sql.append("A.SENDERACQUIRE || '-' || (SELECT B.BGBK_NAME FROM BANK_GROUP B WHERE A.SENDERACQUIRE = B.BGBK_ID) AS SENDERACQUIRE_NAME, ");
			sql.append("A.INACQUIRE || '-' || (SELECT B.BGBK_NAME FROM BANK_GROUP B WHERE A.INACQUIRE = B.BGBK_ID) AS INACQUIRE_NAME, ");
			sql.append("A.OUTACQUIRE || '-' || (SELECT B.BGBK_NAME FROM BANK_GROUP B WHERE A.OUTACQUIRE = B.BGBK_ID) AS OUTACQUIRE_NAME, ");
			sql.append("A.WOACQUIRE || '-' || (SELECT B.BGBK_NAME FROM BANK_GROUP B WHERE A.WOACQUIRE = B.BGBK_ID) AS WOACQUIRE_NAME, ");
			sql.append("A.SENDERHEAD || '-' || (SELECT B.BGBK_NAME FROM BANK_GROUP B WHERE A.SENDERHEAD = B.BGBK_ID) AS SENDERHEAD_NAME, ");
			sql.append("A.INHEAD || '-' || (SELECT B.BGBK_NAME FROM BANK_GROUP B WHERE A.INHEAD = B.BGBK_ID) AS INHEAD_NAME, ");
			sql.append("A.OUTHEAD || '-' || (SELECT B.BGBK_NAME FROM BANK_GROUP B WHERE A.OUTHEAD = B.BGBK_ID) AS OUTHEAD_NAME, ");
			sql.append("A.WOHEAD || '-' || (SELECT B.BGBK_NAME FROM BANK_GROUP B WHERE A.WOHEAD = B.BGBK_ID) AS WOHEAD_NAME, ");
			sql.append("(CASE A.RESULTSTATUS WHEN 'R' THEN 0 WHEN 'A' THEN A.NEWSENDERFEE ELSE (CASE COALESCE(OP.RESULTCODE,'00') WHEN '00' THEN A.NEWSENDERFEE ELSE (CASE A.ACCTCODE WHEN '0' THEN A.NEWSENDERFEE ELSE 0 END) END) END) AS NEWSENDERFEE, ");
			sql.append("(CASE A.RESULTSTATUS WHEN 'R' THEN 0 WHEN 'A' THEN A.NEWINFEE ELSE (CASE COALESCE(OP.RESULTCODE,'00') WHEN '00' THEN A.NEWINFEE ELSE (CASE A.ACCTCODE WHEN '0' THEN A.NEWINFEE ELSE 0 END) END) END) AS NEWINFEE, ");
			sql.append("(CASE A.RESULTSTATUS WHEN 'R' THEN 0 WHEN 'A' THEN A.NEWOUTFEE ELSE (CASE COALESCE(OP.RESULTCODE,'00') WHEN '00' THEN A.NEWOUTFEE ELSE (CASE A.ACCTCODE WHEN '0' THEN A.NEWOUTFEE ELSE 0 END) END) END) AS NEWOUTFEE, ");
			sql.append("(CASE A.RESULTSTATUS WHEN 'R' THEN 0 WHEN 'A' THEN A.NEWWOFEE ELSE (CASE COALESCE(OP.RESULTCODE,'00') WHEN '00' THEN A.NEWWOFEE ELSE (CASE A.ACCTCODE WHEN '0' THEN A.NEWWOFEE ELSE 0 END) END) END) AS NEWWOFEE, ");
			sql.append("(CASE A.RESULTSTATUS WHEN 'R' THEN 0 WHEN 'A' THEN A.NEWEACHFEE ELSE (CASE COALESCE(OP.RESULTCODE,'00') WHEN '00' THEN A.NEWEACHFEE ELSE (CASE A.ACCTCODE WHEN '0' THEN A.NEWEACHFEE ELSE 0 END) END) END) AS NEWEACHFEE, ");
			//20220210新增
			sql.append("(CASE A.RESULTSTATUS WHEN 'R' THEN 0 WHEN 'A' THEN A.NEWEXTENDFEE ELSE (CASE COALESCE(OP.RESULTCODE,'00') WHEN '00' THEN A.NEWEXTENDFEE ELSE (CASE A.ACCTCODE WHEN '0' THEN A.NEWEXTENDFEE ELSE 0 END) END) END) AS NEWEXTENDFEE, ");
			sql.append(" A.EXTENDFEE AS EXTENDFEE , ");
			//20220210新增end
			//20200824新增start
			sql.append("(CASE A.RESULTSTATUS WHEN 'R' THEN 0 WHEN 'A' THEN A.SENDERFEE_NW ELSE (CASE COALESCE(OP.RESULTCODE,'00') WHEN '00' THEN A.SENDERFEE_NW ELSE (CASE A.ACCTCODE WHEN '0' THEN A.SENDERFEE_NW ELSE 0 END) END) END) AS NEWSENDERFEE_NW, ");
			sql.append("(CASE A.RESULTSTATUS WHEN 'R' THEN 0 WHEN 'A' THEN A.INFEE_NW ELSE (CASE COALESCE(OP.RESULTCODE,'00') WHEN '00' THEN A.INFEE_NW ELSE (CASE A.ACCTCODE WHEN '0' THEN A.INFEE_NW ELSE 0 END) END) END) AS NEWINFEE_NW, ");
			sql.append("(CASE A.RESULTSTATUS WHEN 'R' THEN 0 WHEN 'A' THEN A.OUTFEE_NW ELSE (CASE COALESCE(OP.RESULTCODE,'00') WHEN '00' THEN A.OUTFEE_NW ELSE (CASE A.ACCTCODE WHEN '0' THEN A.OUTFEE_NW ELSE 0 END) END) END) AS NEWOUTFEE_NW, ");
			sql.append("(CASE A.RESULTSTATUS WHEN 'R' THEN 0 WHEN 'A' THEN A.WOFEE_NW ELSE (CASE COALESCE(OP.RESULTCODE,'00') WHEN '00' THEN A.WOFEE_NW ELSE (CASE A.ACCTCODE WHEN '0' THEN A.WOFEE_NW ELSE 0 END) END) END) AS NEWWOFEE_NW, ");
			sql.append("(CASE A.RESULTSTATUS WHEN 'R' THEN 0 WHEN 'A' THEN A.EACHFEE_NW ELSE (CASE COALESCE(OP.RESULTCODE,'00') WHEN '00' THEN A.EACHFEE_NW ELSE (CASE A.ACCTCODE WHEN '0' THEN A.EACHFEE_NW ELSE 0 END) END) END) AS NEWEACHFEE_NW, ");
			sql.append(" (CASE CAST(A.FEE_TYPE AS VARCHAR(1)) WHEN ' ' THEN '' ELSE CAST(A.FEE_TYPE AS VARCHAR(1)) END) AS FEE_TYPE ,");
			sql.append(" (CASE CAST(A.FEE_LVL_TYPE AS VARCHAR(1)) WHEN ' ' THEN '' ELSE CAST(A.FEE_LVL_TYPE AS VARCHAR(1)) END) AS FEE_LVL_TYPE ,");
			//20200824新增end
			
			
			sql.append("A.SENDERID,A.RECEIVERID,TRANSLATE('abcd-ef-gh',A.REFUNDDEADLINE,'abcdefgh') AS REFUNDDEADLINE, ");
//			sql.append("A.NEWSENDERFEE AS SENDERFEE, ");
//			sql.append("A.NEWINFEE AS INFEE, ");
//			sql.append("A.NEWOUTFEE AS OUTFEE, ");
//			sql.append("A.NEWEACHFEE AS EACHFEE,A.SENDERID,A.RECEIVERID,A.REFUNDDEADLINE, ");
			sql.append("A.TXID || '-' || TC.TXN_NAME AS TXN_NAME, A.NEWTXAMT AS NEWTXAMT, A.SENDERSTATUS, ");
			
			/*
			sql.append("(CASE WHEN (A.RESULTSTATUS = 'R' OR (A.RESULTSTATUS = 'P' AND ONP.RESULTCODE = '01')) THEN '0' ELSE (INTEGER(SUBSTR((CASE LENGTH(TRIM(COALESCE(A.FEE, '00000'))) WHEN 0 THEN '00000' ELSE COALESCE(A.FEE, '00000') END),1,3)) || '.' || ");
			sql.append("SUBSTR((CASE LENGTH(TRIM(COALESCE(A.FEE, '00000'))) WHEN 0 THEN '00000' ELSE COALESCE(A.FEE, '00000') END),4,2)) END) AS FEE, ");
			*/
			//sql.append("INTEGER(SUBSTR(RIGHT((REPEAT('0',5) || A.NEWFEE), 5),1,3)) || '.' || SUBSTR(RIGHT((REPEAT('0',5) || A.NEWFEE), 5),4,2) AS FEE, ");
			sql.append("(CASE A.RESULTSTATUS WHEN 'R' THEN 0 WHEN 'A' THEN A.NEWFEE ELSE (CASE COALESCE(OP.RESULTCODE,'00') WHEN '00' THEN A.NEWFEE ELSE (CASE A.ACCTCODE WHEN '0' THEN A.NEWFEE ELSE 0 END) END) END) AS NEWFEE, ");
			sql.append("(CASE A.RESULTSTATUS WHEN 'R' THEN 0 WHEN 'A' THEN A.HANDLECHARGE_NW ELSE (CASE COALESCE(OP.RESULTCODE,'00') WHEN '00' THEN A.HANDLECHARGE_NW ELSE (CASE A.ACCTCODE WHEN '0' THEN A.HANDLECHARGE_NW ELSE 0 END) END) END) AS NEWFEE_NW, ");
			
			sql.append("A.SENDERBANKID || '-' || (SELECT B.BRBK_NAME FROM BANK_BRANCH B WHERE A.SENDERBANKID = B.BRBK_ID) AS SENDERBANKID_NAME, ");
			sql.append("A.INBANKID || '-' || (SELECT B.BRBK_NAME FROM BANK_BRANCH B WHERE A.INBANKID = B.BRBK_ID) AS INBANKID_NAME, ");
			sql.append("A.OUTBANKID || '-' || (SELECT B.BRBK_NAME FROM BANK_BRANCH B WHERE A.OUTBANKID = B.BRBK_ID) AS OUTBANKID_NAME, ");
			sql.append("A.WOBANKID || '-' || (SELECT B.BRBK_NAME FROM BANK_BRANCH B WHERE A.WOBANKID = B.BRBK_ID) AS WOBANKID_NAME, ");
			sql.append("TRANSLATE('abcd-ef-gh', A.BIZDATE, 'abcdefgh') AS BIZDATE,TRANSLATE('abcd-ef-gh ij:kl:mn', A.EACHDT, 'abcdefghijklmn') AS EACHDT,A.CLEARINGPHASE,A.INACCTNO,A.OUTACCTNO,A.INID,A.OUTID,A.ACCTBAL,A.AVAILBAL,A.CHECKTYPE,A.MERCHANTID,A.ORDERNO,A.TRMLID,A.TRMLCHECK,A.TRMLMCC,A.BANKRSPMSG,A.RRN, ");
			
			//20150319 by 李建利  「交易資料查詢」、「未完成交易資料查詢」的檢視明細的「交易結果」顯示最初交易結果即可
			//sql.append("(CASE A.NEWRESULT WHEN 'A' THEN '成功' WHEN 'R' THEN '失敗' ELSE COALESCE((CASE OP.RESULTCODE WHEN '01' THEN '失敗' ELSE '成功' END),(CASE A.SENDERSTATUS WHEN '1' THEN '處理中' ELSE '未完成' END)) END) AS RESP, ");
			sql.append("(CASE A.RESULTSTATUS WHEN 'A' THEN '成功' WHEN 'R' THEN '失敗' ELSE (CASE A.SENDERSTATUS WHEN '1' THEN '處理中' ELSE '未完成' END) END) AS RESP, ");
			
			sql.append("RC1 || '-' || (SELECT B.ERR_DESC FROM TXN_ERROR_CODE B WHERE A.RC1=B.ERROR_ID) ERR_DESC1, ");
			sql.append("RC2 || '-' || (SELECT B.ERR_DESC FROM TXN_ERROR_CODE B WHERE A.RC2=B.ERROR_ID) ERR_DESC2, ");
			sql.append("RC3 || '-' || (SELECT B.ERR_DESC FROM TXN_ERROR_CODE B WHERE A.RC3=B.ERROR_ID) ERR_DESC3, ");
			sql.append("RC4 || '-' || (SELECT B.ERR_DESC FROM TXN_ERROR_CODE B WHERE A.RC4=B.ERROR_ID) ERR_DESC4, ");
			sql.append("RC5 || '-' || (SELECT B.ERR_DESC FROM TXN_ERROR_CODE B WHERE A.RC5=B.ERROR_ID) ERR_DESC5, ");
			sql.append("RC6 || '-' || (SELECT B.ERR_DESC FROM TXN_ERROR_CODE B WHERE A.RC6=B.ERROR_ID) ERR_DESC6, TRANSLATE('abcd-ef-gh ij:kl:mn.opq',UPDATEDT,'abcdefghijklmnopq') AS UPDATEDT, ");
			sql.append("A.BILLTYPE, OBA.BILLDATA, A.CHARGETYPE, A.TOLLID, A.BILLFLAG, OBA.CHECKDATA,");
			sql.append("A.PFCLASS || '-' || (SELECT BT.BILL_TYPE_NAME FROM BILL_TYPE BT WHERE A.PFCLASS = BT.BILL_TYPE_ID) AS PFCLASS, ");
			sql.append("OBA.USERNO, A.CONMEMO, OBA.SENDERDATA, OBA.COMPANYID, A.MERCHANTID, A.ORDERNO, A.TRMLID, OBA.OTXAMT, COALESCE(TRANSLATE('abcd-ef-gh',OBA.OTXDATE,'abcdefgh'),'') AS OTXDATE, OBA.OTRMLID, OBA.OMERCHANTID, OBA.OORDERNO, OBA.PAN, OBA.OPAN, OBA.PSN, OBA.OPSN, ");
			sql.append("COALESCE(TRANSLATE('abcd-ef-gh',VARCHAR(OP.BIZDATE),'abcdefgh'),'') AS NEWBIZDATE ");
			sql.append(" , COALESCE(OP.CLEARINGPHASE,'') AS NEWCLRPHASE, COALESCE(OP.RESULTCODE,'eACH') AS RESULTCODE ");
			sql.append(" ,TRANSLATE('abcd-ef-gh', COALESCE(A.FLBIZDATE,''), 'abcdefgh') AS FLBIZDATE ,COALESCE(VARCHAR(A.FLPROCSEQ),'') AS FLPROCSEQ ");
			sql.append(" ,COALESCE(A.FLBATCHSEQ,'') AS FLBATCHSEQ, COALESCE(A.DATASEQ,'') AS DATASEQ ");
			
			sql.append("FROM VW_ONBLOCKTAB A LEFT JOIN EACH_TXN_CODE ETC ON A.PCODE = ETC.EACH_TXN_ID "); 
			sql.append("LEFT JOIN TXN_CODE TC ON A.TXID = TC.TXN_ID ");
			sql.append("LEFT JOIN ONBLOCKAPPENDTAB OBA ON A.TXDATE = OBA.TXDATE AND A.STAN = OBA.STAN ");
			sql.append("LEFT JOIN ONPENDINGTAB OP ON OP.OTXDATE = A.TXDATE AND OP.OSTAN = A.STAN ");
			condition += "WHERE ";
			if(!txdate.equals("")){
				condition += " A.TXDATE='"+txdate+"'";
				condition += " AND ";
			}
			if(!stan.equals("")){
				condition += " A.STAN='"+stan+"'";
			}
			sql.append(condition);
			System.out.println("sql===>"+sql.toString());
			po = vwOnBlockTabRepository.getDetail(sql.toString(),txdate,stan);
		}catch(Exception e){
			e.printStackTrace();
		}
					
		return po;
	}
public Map getNewFeeDetail(String bizdate,String txid,String senderid , String senderbankid , String txamt){
	if(txid.indexOf("-")!=-1) {
		txid = txid.substring(0,txid.indexOf("-"));
	}
	if(senderid.indexOf("-")!=-1) {
		senderid = senderid.substring(0,senderid.indexOf("-"));
	}
	if(senderbankid.indexOf("-")!=-1) {
		senderbankid = senderbankid.substring(0,senderbankid.indexOf("-"));
	}
	return onBlockTabRepository.getNewFeeDetail(bizdate,txid,senderid , senderbankid , txamt);
}

}

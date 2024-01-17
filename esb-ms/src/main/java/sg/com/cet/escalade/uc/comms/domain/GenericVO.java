package sg.com.cet.escalade.uc.comms.domain;

import java.util.Arrays;

import sg.com.cet.escalade.common.BaseVO;

/**
 * <p>
 * Title: Escalade
 * </p>
 * <p>
 * Description: Taxi Dispatch and Bureau Service System
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: CET
 * </p>
 *
 * @author not attributable
 * @version 1.0
 */
public class GenericVO extends BaseVO {
    
    /**
     * 
     */
    private static final long serialVersionUID = -7725773171700538447L;
    
	private String mobileId;
	private byte[] byteArray;
	private int byteArraySize;
        public static int counter = 1;
        private int msgCount;

	public GenericVO() {
          msgCount = counter++;
	}

	public byte[] getByteArray() {
		return byteArray;
	}
	public void setByteArray(byte[] byteArray) {
		this.byteArray = byteArray;
	}
	public String getMobileId() {
		return mobileId;
	}
	public void setMobileId(String mobileId) {
		this.mobileId = mobileId;
	}
	public int getByteArraySize() {
		return byteArraySize;
	}
	public void setByteArraySize(int byteArraySize) {
		this.byteArraySize = byteArraySize;
	}

        public int getMsgCount() {
          return msgCount;
        }

		@Override
		public String toString() {
			return "GenericVO [mobileId=" + mobileId + ", byteArray=" + new String(byteArray) + ", byteArraySize="
					+ byteArraySize + ", msgCount=" + msgCount + "]";
		}
        
}
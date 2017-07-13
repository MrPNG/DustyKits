package br.com.dusty.dkits.util.protocol;

public enum EnumProtocolVersion {
	
	PRE_1_7(0, 3, "pre-1.7", EnumProtocolSafety.TERRIBLE),
	RELEASE_1_7_2(4, "1.7.2-1.7.5", EnumProtocolSafety.BAD),
	RELEASE_1_7_10(5, "1.7.6-1.7.10", EnumProtocolSafety.GOOD),
	SNAPSHOT_1_8(6, 46, "snapshot_1.8", EnumProtocolSafety.TERRIBLE),
	RELEASE_1_8(47, "1.8.0-1.8.9", EnumProtocolSafety.REGULAR),
	SNAPSHOT_1_9(48, 106, "snapshot_1.9", EnumProtocolSafety.TERRIBLE),
	RELEASE_1_9(107, "1.9.0", EnumProtocolSafety.TERRIBLE),
	RELEASE_1_9_1(108, "1.9.1", EnumProtocolSafety.TERRIBLE),
	RELEASE_1_9_2(109, "1.9.2", EnumProtocolSafety.REGULAR),
	RELEASE_1_9_4(110, "1.9.3-1.9.4", EnumProtocolSafety.GOOD),
	SNAPSHOT_1_10(201, 205, "snapshot_1.10", EnumProtocolSafety.TERRIBLE),
	RELEASE_1_10(210, "1.10.0-1.10.2", EnumProtocolSafety.GOOD),
	SNAPSHOT_1_11(301, 314, "snapshot_1.11", EnumProtocolSafety.TERRIBLE),
	RELEASE_1_11(315, "1.11.0", EnumProtocolSafety.REGULAR),
	RELEASE_1_11_2(316, "1.11.1-1.11.2", EnumProtocolSafety.GOOD),
	SNAPSHOT_1_12(317, 334, "snapshot_1.12", EnumProtocolSafety.TERRIBLE),
	RELEASE_1_12(335, "1.12", EnumProtocolSafety.EXCELLENT);
	
	
	public int min, max;
	public String name;
	public EnumProtocolSafety safety;
	
	EnumProtocolVersion(int version, String name, EnumProtocolSafety safety) {
		this.min = version;
		this.max = version;
		this.name = name;
		this.safety = safety;
	}
	
	EnumProtocolVersion(int min, int max, String name, EnumProtocolSafety safety) {
		this.min = min;
		this.max = max;
		this.name = name;
		this.safety = safety;
	}
	
	public static EnumProtocolVersion byVersionNumber(int version) {
		for(EnumProtocolVersion protocolVersion : values()){
			if(protocolVersion.min <= version && protocolVersion.max >= version)
				return protocolVersion;
		}
		
		return null;
	}
	
	public boolean isGreaterThan(EnumProtocolVersion protocolVersion) {
		return protocolVersion.max < min;
	}
	
	public boolean isGreaterThanOrEquals(EnumProtocolVersion protocolVersion) {
		return this == protocolVersion || protocolVersion.max < min;
	}
	
	public boolean isLowerThan(EnumProtocolVersion protocolVersion) {
		return protocolVersion.min > max;
	}
	
	public boolean isLowerThanOrEquals(EnumProtocolVersion protocolVersion) {
		return this == protocolVersion || protocolVersion.min > max;
	}
}

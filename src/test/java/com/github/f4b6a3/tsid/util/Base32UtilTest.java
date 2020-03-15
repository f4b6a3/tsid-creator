package com.github.f4b6a3.tsid.util;

import static org.junit.Assert.*;

import java.math.BigInteger;

import org.junit.Test;

import com.github.f4b6a3.tsid.util.Base32Util;
import com.github.f4b6a3.tsid.util.Base32Util.Base32UtilException;

public class Base32UtilTest {

	private static final String LONG_TEXT = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Cras finibus fringilla sem, ac dictum dui mollis vitae. Suspendisse id euismod nunc. Curabitur mollis felis nec scelerisque eleifend. Curabitur eleifend, eros sed pellentesque egestas, dolor risus sodales sapien, ut finibus urna nisi id massa. Maecenas facilisis accumsan vestibulum. Fusce et sapien sed justo lacinia efficitur. Aenean libero mi, auctor nec rutrum at, tincidunt eu nisi. Curabitur urna quam, lobortis vel sem ullamcorper, dignissim varius metus. Proin congue nunc ut nisl hendrerit, vel euismod nunc auctor. Nunc metus arcu, lacinia ac dolor quis, dignissim tempor ante. Nunc condimentum tortor nec urna mattis venenatis. Vestibulum elit magna, sollicitudin eu iaculis at, faucibus eu lacus. Curabitur ultrices, elit non dictum vulputate, ligula elit egestas elit, vitae tincidunt lacus massa eu justo. Nam a velit ullamcorper, tincidunt tortor in, sodales velit. Suspendisse non mi quis lacus lacinia ultrices at at massa. Nullam tempus turpis duis.";

	private static final String LONG_TEXT_BASE_32 = "JRXXEZLNEBUXA43VNUQGI33MN5ZCA43JOQQGC3LFOQWCAY3PNZZWKY3UMV2HK4RAMFSGS4DJONRWS3THEBSWY2LUFYQEG4TBOMQGM2LONFRHK4ZAMZZGS3THNFWGYYJAONSW2LBAMFRSAZDJMN2HK3JAMR2WSIDNN5WGY2LTEB3GS5DBMUXCAU3VONYGK3TENFZXGZJANFSCAZLVNFZW233EEBXHK3TDFYQEG5LSMFRGS5DVOIQG233MNRUXGIDGMVWGS4ZANZSWGIDTMNSWYZLSNFZXC5LFEBSWYZLJMZSW4ZBOEBBXK4TBMJUXI5LSEBSWYZLJMZSW4ZBMEBSXE33TEBZWKZBAOBSWY3DFNZ2GK43ROVSSAZLHMVZXIYLTFQQGI33MN5ZCA4TJON2XGIDTN5SGC3DFOMQHGYLQNFSW4LBAOV2CAZTJNZUWE5LTEB2XE3TBEBXGS43JEBUWIIDNMFZXGYJOEBGWCZLDMVXGC4ZAMZQWG2LMNFZWS4ZAMFRWG5LNONQW4IDWMVZXI2LCOVWHK3JOEBDHK43DMUQGK5BAONQXA2LFNYQHGZLEEBVHK43UN4QGYYLDNFXGSYJAMVTGM2LDNF2HK4ROEBAWK3TFMFXCA3DJMJSXE3ZANVUSYIDBOVRXI33SEBXGKYZAOJ2XI4TVNUQGC5BMEB2GS3TDNFSHK3TUEBSXKIDONFZWSLRAIN2XEYLCNF2HK4RAOVZG4YJAOF2WC3JMEBWG6YTPOJ2GS4ZAOZSWYIDTMVWSA5LMNRQW2Y3POJYGK4RMEBSGSZ3ONFZXG2LNEB3GC4TJOVZSA3LFOR2XGLRAKBZG62LOEBRW63THOVSSA3TVNZRSA5LUEBXGS43MEBUGK3TEOJSXE2LUFQQHMZLMEBSXK2LTNVXWIIDOOVXGGIDBOVRXI33SFYQE45LOMMQG2ZLUOVZSAYLSMN2SYIDMMFRWS3TJMEQGCYZAMRXWY33SEBYXK2LTFQQGI2LHNZUXG43JNUQHIZLNOBXXEIDBNZ2GKLRAJZ2W4YZAMNXW4ZDJNVSW45DVNUQHI33SORXXEIDOMVRSA5LSNZQSA3LBOR2GS4ZAOZSW4ZLOMF2GS4ZOEBLGK43UNFRHK3DVNUQGK3DJOQQG2YLHNZQSYIDTN5WGY2LDNF2HKZDJNYQGK5JANFQWG5LMNFZSAYLUFQQGMYLVMNUWE5LTEBSXKIDMMFRXK4ZOEBBXK4TBMJUXI5LSEB2WY5DSNFRWK4ZMEBSWY2LUEBXG63RAMRUWG5DVNUQHM5LMOB2XIYLUMUWCA3DJM52WYYJAMVWGS5BAMVTWK43UMFZSAZLMNF2CYIDWNF2GCZJAORUW4Y3JMR2W45BANRQWG5LTEBWWC43TMEQGK5JANJ2XG5DPFYQE4YLNEBQSA5TFNRUXIIDVNRWGC3LDN5ZHAZLSFQQHI2LOMNUWI5LOOQQHI33SORXXEIDJNYWCA43PMRQWYZLTEB3GK3DJOQXCAU3VONYGK3TENFZXGZJANZXW4IDNNEQHC5LJOMQGYYLDOVZSA3DBMNUW42LBEB2WY5DSNFRWK4ZAMF2CAYLUEBWWC43TMEXCATTVNRWGC3JAORSW24DVOMQHI5LSOBUXGIDEOVUXGLQ=";
	private static final String LONG_TEXT_BASE_32_HEX = "9HNN4PBD41KN0SRLDKG68RRCDTP20SR9EGG62RB5EGM20ORFDPPMAORKCLQ7ASH0C5I6IS39EDHMIRJ741IMOQBK5OG46SJ1ECG6CQBED5H7ASP0CPP6IRJ7D5M6OO90EDIMQB10C5HI0P39CDQ7AR90CHQMI83DDTM6OQBJ41R6IT31CKN20KRLEDO6ARJ4D5PN6P90D5I20PBLD5PMQRR441N7ARJ35OG46TBIC5H6IT3LE8G6QRRCDHKN6836CLM6ISP0DPIM683JCDIMOPBID5PN2TB541IMOPB9CPIMSP1E411NASJ1C9KN8TBI41IMOPB9CPIMSP1C41IN4RRJ41PMAP10E1IMOR35DPQ6ASRHELII0PB7CLPN8OBJ5GG68RRCDTP20SJ9EDQN683JDTI62R35ECG76OBGD5IMSB10ELQ20PJ9DPKM4TBJ41QN4RJ141N6ISR941KM883DC5PN6O9E416M2PB3CLN62SP0CPGM6QBCD5PMISP0C5HM6TBDEDGMS83MCLPN8QB2ELM7AR9E4137ASR3CKG6AT10EDGN0QB5DOG76PB441L7ASRKDSG6OOB3D5N6IO90CLJ6CQB3D5Q7ASHE410MARJ5C5N20R39C9IN4RP0DLKIO831ELHN8RRI41N6AOP0E9QN8SJLDKG62T1C41Q6IRJ3D5I7ARJK41INA83ED5PMIBH08DQN4OB2D5Q7ASH0ELP6SO90E5QM2R9C41M6UOJFE9Q6ISP0EPIMO83JCLMI0TBCDHGMQORFE9O6ASHC41I6IPRED5PN6QBD41R62SJ9ELPI0RB5EHQN6BH0A1P6UQBE41HMURJ7ELII0RJLDPHI0TBK41N6ISRC41K6ARJ4E9IN4QBK5GG7CPBC41INAQBJDLNM883EELN66831ELHN8RRI5OG4STBECCG6QPBKELPI0OBICDQIO83CC5HMIRJ9C4G62OP0CHNMORRI41ONAQBJ5GG68QB7DPKN6SR9DKG78PBDE1NN4831DPQ6ABH09PQMSOP0CDNMSP39DLIMST3LDKG78RRIEHNN483ECLHI0TBIDPGI0RB1EHQ6ISP0EPIMSPBEC5Q6ISPE41B6ASRKD5H7AR3LDKG6AR39EGG6QOB7DPGIO83JDTM6OQB3D5Q7AP39DOG6AT90D5GM6TBCD5PI0OBK5GG6COBLCDKM4TBJ41INA83CC5HNASPE411NASJ1C9KN8TBI41QMOT3ID5HMASPC41IMOQBK41N6URH0CHKM6T3LDKG7CTBCE1QN8OBKCKM20R39CTQMOO90CLM6IT10CLJMASRKC5PI0PBCD5Q2O83MD5Q62P90EHKMSOR9CHQMST10DHGM6TBJ41MM2SRJC4G6AT90D9QN6T3F5OG4SOBD41GI0TJ5DHKN883LDHM62RB3DTP70PBI5GG78QBECDKM8TBEEGG78RRIEHNN4839DOM20SRFCHGMOPBJ41R6AR39EGN20KRLEDO6ARJ4D5PN6P90DPNMS83DD4G72TB9ECG6OOB3ELPI0R31CDKMSQB141QMOT3ID5HMASP0C5Q20OBK41MM2SRJC4N20JJLDHM62R90EHIMQS3LECG78TBIE1KN6834ELKN6BG=";
	private static final String LONG_TEXT_BASE_32_Z = "jtzzr3mprbwzyh5ipwoge55cp73nyh5jqoogn5mfqosnya5xp33ska5wci48khtycf1g1hdjqpts15u8rb1sa4mwfaorghubqcogc4mqpft8kh3yc33g15u8pfsgaajyqp1s4mbycft1y3djcp48k5jyct4s1edpp7sga4murb5g17dbcwznyw5iqpagk5urpf3zg3jypf1ny3mipf3s455rrbz8k5udfaorg7m1cftg17diqeog455cptwzgedgcisg1h3yp31sgeducp1sa3m1pf3zn7mfrb1sa3mjc31sh3bqrbbzkhubcjwze7m1rb1sa3mjc31sh3bcrb1zr55urb3sk3byqb1sa5dfp34gkh5tqi11y3m8ci3zeamufooge55cp73nyhujqp4zgedup71gn5dfqco8gamopf1shmbyqi4ny3ujp3wsr7murb4zr5ubrbzg1h5jrbwseedpcf3zgajqrbgsn3mdcizgnh3yc3osg4mcpf3s1h3ycftsg7mpqposhedsci3ze4mnqis8k5jqrbd8kh5dcwogk7byqpozy4mfpao8g3mrrbi8kh5wphogaamdpfzg1ajyciugc4mdpf48khtqrbysk5ufcfzny5djcj1zr53ypiw1aedbqitze551rbzgka3yqj4zehuipwogn7bcrb4g15udpf18k5uwrb1zkedqpf3s1mtyep4zramnpf48khtyqi3ghajyqf4sn5jcrbsg6auxqj4g1h3yq31saeducis1y7mcptos4a5xqjagkhtcrb1g135qpf3zg4mprb5gnhujqi31y5mfqt4zgmtykb3g64mqrbts65u8qi11y5uip3t1y7mwrbzg1h5crbwgk5urqj1zr4mwfoo8c3mcrb1zk4mupizseedqqizggedbqitze551faorh7mqccog43mwqi31yam1cp41aedccfts15ujcrogna3yctzsa551rbazk4mufooge4m8p3wzgh5jpwo8e3mpqbzzredbp34gkmtyj34sha3ycpzsh3djpi1sh7dipwo8e551qtzzredqcit1y7m1p3o1y5mbqt4g1h3yq31sh3mqcf4g1h3qrbmgkh5wpft8k5dipwogk5djqoog4am8p3o1aedup7sga4mdpf48k3djpaogk7jypfosg7mcpf31yamwfoogcamicpwsr7murb1zkedccftzkh3qrbbzkhubcjwze7m1rb4sa7d1pftskh3crb1sa4mwrbzg65tyctwsg7dipwo8c7mcqb4zeamwcwsny5djc74saajycisg17byciuskh5wcf31y3mcpf4naedspf4gn3jyqtwsha5jct4sh7byptosg7murbssnh5ucrogk7jypj4zg7dxfaorhamprbo1y7ufptwzeediptsgn5mdp738y3m1foo8e4mqcpwse7mqqoo8e551qtzzredjpasnyh5xctosa3murb5gk5djqoznyw5iqpagk5urpf3zg3jyp3zshedppro8n7mjqcogaamdqi31y5dbcpwsh4mbrb4sa7d1pftskh3ycf4nyamwrbssnh5ucrznyuuiptsgn5jyqt1s4hdiqco8e7m1qbwzgedrqiwzgmo";
	private static final String LONG_TEXT_BASE_32_CROCKFORD = "9HQQ4SBD41MQ0WVNDMG68VVCDXS20WV9EGG62VB5EGP20RVFDSSPARVMCNT7AWH0C5J6JW39EDHPJVK741JPRTBM5RG46WK1ECG6CTBED5H7AWS0CSS6JVK7D5P6RR90EDJPTB10C5HJ0S39CDT7AV90CHTPJ83DDXP6RTBK41V6JX31CMQ20MVNEDR6AVK4D5SQ6S90D5J20SBND5SPTVV441Q7AVK35RG46XBJC5H6JX3NE8G6TVVCDHMQ6836CNP6JWS0DSJP683KCDJPRSBJD5SQ2XB541JPRSB9CSJPWS1E411QAWK1C9MQ8XBJ41JPRSB9CSJPWS1C41JQ4VVK41SPAS10E1JPRV35DST6AWVHENJJ0SB7CNSQ8RBK5GG68VVCDXS20WK9EDTQ683KDXJ62V35ECG76RBGD5JPWB10ENT20SK9DSMP4XBK41TQ4VK141Q6JWV941MP883DC5SQ6R9E416P2SB3CNQ62WS0CSGP6TBCD5SPJWS0C5HP6XBDEDGPW83PCNSQ8TB2ENP7AV9E4137AWV3CMG6AX10EDGQ0TB5DRG76SB441N7AWVMDWG6RRB3D5Q6JR90CNK6CTB3D5T7AWHE410PAVK5C5Q20V39C9JQ4VS0DNMJR831ENHQ8VVJ41Q6ARS0E9TQ8WKNDMG62X1C41T6JVK3D5J7AVKM41JQA83ED5SPJBH08DTQ4RB2D5T7AWH0ENS6WR90E5TP2V9C41P6YRKFE9T6JWS0ESJPR83KCNPJ0XBCDHGPTRVFE9R6AWHC41J6JSVED5SQ6TBD41V62WK9ENSJ0VB5EHTQ6BH0A1S6YTBE41HPYVK7ENJJ0VKNDSHJ0XBM41Q6JWVC41M6AVK4E9JQ4TBM5GG7CSBC41JQATBKDNQP883EENQ66831ENHQ8VVJ5RG4WXBECCG6TSBMENSJ0RBJCDTJR83CC5HPJVK9C4G62RS0CHQPRVVJ41RQATBK5GG68TB7DSMQ6WV9DMG78SBDE1QQ4831DST6ABH09STPWRS0CDQPWS39DNJPWX3NDMG78VVJEHQQ483ECNHJ0XBJDSGJ0VB1EHT6JWS0ESJPWSBEC5T6JWSE41B6AWVMD5H7AV3NDMG6AV39EGG6TRB7DSGJR83KDXP6RTB3D5T7AS39DRG6AX90D5GP6XBCD5SJ0RBM5GG6CRBNCDMP4XBK41JQA83CC5HQAWSE411QAWK1C9MQ8XBJ41TPRX3JD5HPAWSC41JPRTBM41Q6YVH0CHMP6X3NDMG7CXBCE1TQ8RBMCMP20V39CXTPRR90CNP6JX10CNKPAWVMC5SJ0SBCD5T2R83PD5T62S90EHMPWRV9CHTPWX10DHGP6XBK41PP2WVKC4G6AX90D9TQ6X3F5RG4WRBD41GJ0XK5DHMQ883NDHP62VB3DXS70SBJ5GG78TBECDMP8XBEEGG78VVJEHQQ4839DRP20WVFCHGPRSBK41V6AV39EGQ20MVNEDR6AVK4D5SQ6S90DSQPW83DD4G72XB9ECG6RRB3ENSJ0V31CDMPWTB141TPRX3JD5HPAWS0C5T20RBM41PP2WVKC4Q20KKNDHP62V90EHJPTW3NECG78XBJE1MQ6834ENMQ6BG";

	private static final String SHORT_TEXT = "Lorem ipsum dolor sit volutpat.";
	private static final String SHORT_TEXT_BASE_32 = "JRXXEZLNEBUXA43VNUQGI33MN5ZCA43JOQQHM33MOV2HAYLUFY======";
	private static final String SHORT_TEXT_BASE_32_HEX = "9HNN4PBD41KN0SRLDKG68RRCDTP20SR9EGG7CRRCELQ70OBK5O======";
	private static final String SHORT_TEXT_BASE_32_Z = "jtzzr3mprbwzyh5ipwoge55cp73nyh5jqoo8c55cqi48yamwfa";
	private static final String SHORT_TEXT_BASE_32_CROCKFORD = "9HQQ4SBD41MQ0WVNDMG68VVCDXS20WV9EGG7CVVCENT70RBM5R";

	private static final String[] WORDS = { "", "f", "fo", "foo", "foob", "fooba", "foobar", "foobarc", "foobarcp",
			"foobarcpt", "foobarcpto" };

	private static final String[] WORDS_BASE_32 = { "", "MY======", "MZXQ====", "MZXW6===", "MZXW6YQ=", "MZXW6YTB",
			"MZXW6YTBOI======", "MZXW6YTBOJRQ====", "MZXW6YTBOJRXA===", "MZXW6YTBOJRXA5A=", "MZXW6YTBOJRXA5DP" };

	private static final String[] WORDS_BASE_32_HEX = { "", "CO======", "CPNG====", "CPNMU===", "CPNMUOG=", "CPNMUOJ1",
			"CPNMUOJ1E8======", "CPNMUOJ1E9HG====", "CPNMUOJ1E9HN0===", "CPNMUOJ1E9HN0T0=", "CPNMUOJ1E9HN0T3F" };

	private static final String[] WORDS_BASE_32_Z = { "", "ca", "c3zo", "c3zs6", "c3zs6ao", "c3zs6aub", "c3zs6aubqe",
			"c3zs6aubqjto", "c3zs6aubqjtzy", "c3zs6aubqjtzy7y", "c3zs6aubqjtzy7dx" };

	private static final String[] WORDS_BASE_32_CROCKFORD = { "", "CR", "CSQG", "CSQPY", "CSQPYRG", "CSQPYRK1",
			"CSQPYRK1E8", "CSQPYRK1E9HG", "CSQPYRK1E9HQ0", "CSQPYRK1E9HQ0X0", "CSQPYRK1E9HQ0X3F" };

	private static final int[] NUMBERS = { 102685630, 725393777, 573697669, 614668535, 790665079, 728958755, 966150230,
			410015018, 605266173, 946077566, 214051168, 775737014, 723003700, 391609366, 147844737, 514081413,
			488279622, 550860813, 611087782, 223492126, 706308515, 158990768, 549042286, 26926303, 775714134, 602886016,
			27282100, 675097356, 641101167, 515280699, 454184468, 371424784, 633917378, 887459583, 792903202, 168552040,
			824806922, 696445335, 653338746, 357696553, 353677217, 972662902, 400738139, 537701151, 202077579,
			110209145, 356152341, 168702810, 684185451, 419840003, 480132486, 308833881, 997154252, 918202260,
			103304091, 328467776, 648729690, 733655121, 645189051, 342500864, 560919543, 509761384, 626871960,
			429248550, 319025067, 507317265, 348303729, 256009160, 660250872, 85224414, 414490625, 355994979, 318005886,
			326093128, 492813589, 569014099, 503350412, 168303553, 801566586, 800368918, 742601973, 395588591,
			257341245, 722366808, 501878988, 200718306, 184948029, 149469829, 992401543, 240364551, 976817281,
			161998068, 515579566, 275182272, 376045488, 899163436, 941443452, 974372015, 934795357, 958806784 };

	private static final String[] NUMBERS_BASE_32 = { "DB5W56", "VTZILR", "RDD3UF", "SKGGHX", "XSBF3X", "VXGBZD",
			"4ZMSCW", "MHAVJK", "SBHIH5", "4GH736", "GMEKLA", "XDZTVW", "VRQKJU", "LVO7AW", "EM73UB", "PKIQUF",
			"ORVDSG", "QNK6AN", "SGY5NG", "GVEOA6", "VBS2ND", "EXUANQ", "QLTODO", "ZVXG7", "XDY5KW", "R66T4A", "2ASVU",
			"UD2KYM", "TDM3LP", "PLNDZ3", "NRETQU", "LCG7QQ", "S4RT6C", "2OLDX7", "XUFPRC", "FAXZTI", "YSTDQK",
			"UYF2MX", "TPCKD2", "KVEBBJ", "KRJL5B", "47TKDW", "L6FR23", "QAZKY7", "GAW5ML", "DJDKDZ", "KTU5AV",
			"FA4M22", "UMPV3L", "MQMQAD", "OJ4PMG", "JGQ3SZ", "5W6XOM", "3LVJ4U", "DCQS43", "JZIBKA", "TKVVC2",
			"V3VMCR", "THJTN3", "KGUJQA", "QW547X", "PGEV3I", "SV2TUY", "MZLUBG", "JQH35L", "PD2DAR", "KMFMLR",
			"HUEY6I", "TVVIHY", "CRI266", "MLJIAB", "KTQDLD", "JPIYT6", "JW7SKI", "OV7PIV", "Q6U52T", "PABBEM",
			"FAQG6B", "X4N332", "X3JKIW", "WEGNHV", "LZIM7P", "HVNNZ5", "VQ44KY", "O6UEGM", "F7NN7C", "FQMFJ5",
			"EOROUF", "5SNWEH", "HFHLAH", "5DSDEB", "E2PZHU", "PLWHVO", "IGN4WA", "LGT75Q", "2ZQJJM", "4B2SL4",
			"5BHPFP", "33PWC5", "4SMOYA" };

	private static final String[] NUMBERS_BASE_32_HEX = { "31TMTU", "LJP8BH", "H33RK5", "IA667N", "NI15RN", "LN61P3",
			"SPCI2M", "C70L9A", "I1787T", "S67VRU", "6C4AB0", "N3PJLM", "LHGA9K", "BLEV0M", "4CVRK1", "FA8GK5",
			"EHL3I6", "GDAU0D", "I6OTD6", "6L4E0U", "L1IQD3", "4NK0DG", "GBJE3E", "PLN6V", "N3OTAM", "HUUJS0", "Q0ILK",
			"K3QAOC", "J3CRBF", "FBD3PR", "DH4JGK", "B26VGG", "ISHJU2", "QEB3NV", "NK5FH2", "50NPJ8", "OIJ3GA",
			"KO5QCN", "JF2A3Q", "AL4119", "AH9BT1", "SVJA3M", "BU5HQR", "G0PAOV", "60MTCB", "393A3P", "AJKT0L",
			"50SCQQ", "KCFLRB", "CGCG03", "E9SFC6", "96GRIP", "TMUNEC", "RBL9SK", "32GISR", "9P81A0", "JALL2Q",
			"LRLC2H", "J79JDR", "A6K9G0", "GMTSVN", "F64LR8", "ILQJKO", "CPBK16", "9G7RTB", "F3Q30H", "AC5CBH",
			"7K4OU8", "JLL87O", "2H8QUU", "CB9801", "AJG3B3", "9F8OJU", "9MVIA8", "ELVF8L", "GUKTQJ", "F0114C",
			"50G6U1", "NSDRRQ", "NR9A8M", "M46D7L", "BP8CVF", "7LDDPT", "LGSSAO", "EUK46C", "5VDDV2", "5GC59T",
			"4EHEK5", "TIDM47", "757B07", "T3I341", "4QFP7K", "FBM7LE", "86DSM0", "B6JVTG", "QPG99C", "S1QIBS",
			"T17F5F", "RRFM2T", "SICEO0" };

	private static final String[] NUMBERS_BASE_32_Z = { "db7s76", "iu3emt", "tdd5wf", "1kgg8z", "z1bf5z", "izgb3d",
			"h3c1ns", "c8yijk", "1b8e87", "hg8956", "gcrkmy", "zd3uis", "itokjw", "miq9ys", "rc95wb", "xkeowf",
			"qtid1g", "opk6yp", "1ga7pg", "girqy6", "ib14pd", "rzwypo", "omuqdq", "3izg9", "zda7ks", "t66uhy", "4y1iw",
			"wd4kac", "udc5mx", "xmpd35", "ptruow", "mng9oo", "1htu6n", "4qmdz9", "zwfxtn", "fyz3ue", "a1udok",
			"waf4cz", "uxnkd4", "kirbbj", "ktjm7b", "h9ukds", "m6ft45", "oy3ka9", "gys7cm", "djdkd3", "kuw7yi",
			"fyhc44", "wcxi5m", "cocoyd", "qjhxcg", "jgo513", "7s6zqc", "5mijhw", "dno1h5", "j3ebky", "ukiin4",
			"i5icnt", "u8jup5", "kgwjoy", "os7h9z", "xgri5e", "1i4uwa", "c3mwbg", "jo857m", "xd4dyt", "kcfcmt",
			"8wra6e", "uiie8a", "nte466", "cmjeyb", "kuodmd", "jxeau6", "js91ke", "qi9xei", "o6w74u", "xybbrc",
			"fyog6b", "zhp554", "z5jkes", "srgp8i", "m3ec9x", "8ipp37", "iohhka", "q6wrgc", "f9pp9n", "focfj7",
			"rqtqwf", "71psr8", "8f8my8", "7d1drb", "r4x38w", "xms8iq", "egphsy", "mgu97o", "43ojjc", "hb41mh",
			"7b8xfx", "55xsn7", "h1cqay" };

	private static final String[] NUMBERS_BASE_32_CROCKFORD = { "31XPXY", "NKS8BH", "H33VM5", "JA667Q", "QJ15VQ",
			"NQ61S3", "WSCJ2P", "C70N9A", "J1787X", "W67ZVY", "6C4AB0", "Q3SKNP", "NHGA9M", "BNEZ0P", "4CZVM1",
			"FA8GM5", "EHN3J6", "GDAY0D", "J6RXD6", "6N4E0Y", "N1JTD3", "4QM0DG", "GBKE3E", "SNQ6Z", "Q3RXAP", "HYYKW0",
			"T0JNM", "M3TARC", "K3CVBF", "FBD3SV", "DH4KGM", "B26ZGG", "JWHKY2", "TEB3QZ", "QM5FH2", "50QSK8", "RJK3GA",
			"MR5TCQ", "KF2A3T", "AN4119", "AH9BX1", "WZKA3P", "BY5HTV", "G0SARZ", "60PXCB", "393A3S", "AKMX0N",
			"50WCTT", "MCFNVB", "CGCG03", "E9WFC6", "96GVJS", "XPYQEC", "VBN9WM", "32GJWV", "9S81A0", "KANN2T",
			"NVNC2H", "K79KDV", "A6M9G0", "GPXWZQ", "F64NV8", "JNTKMR", "CSBM16", "9G7VXB", "F3T30H", "AC5CBH",
			"7M4RY8", "KNN87R", "2H8TYY", "CB9801", "AKG3B3", "9F8RKY", "9PZJA8", "ENZF8N", "GYMXTK", "F0114C",
			"50G6Y1", "QWDVVT", "QV9A8P", "P46D7N", "BS8CZF", "7NDDSX", "NGWWAR", "EYM46C", "5ZDDZ2", "5GC59X",
			"4EHEM5", "XJDP47", "757B07", "X3J341", "4TFS7M", "FBP7NE", "86DWP0", "B6KZXG", "TSG99C", "W1TJBW",
			"X17F5F", "VVFP2X", "WJCER0" };

	private static final String INVALID_CHARS_BASE_32 = "!@#$%*()-_+|,.;:[]0189";
	private static final String INVALID_CHARS_BASE_32_HEX = "!@#$%*()-_+|,.;:[]XYZ";
	private static final String INVALID_CHARS_BASE_32_Z = "!@#$%*()-_+|,.;:[]iv2";
	private static final String INVALID_CHARS_BASE_32_CROCKFORD = "!@#$%*()-_+|,.;:[]ILOU";

	@Test
	public void testToBase32() {
		String result = Base32Util.toBase32(LONG_TEXT);
		assertEquals(LONG_TEXT_BASE_32.length(), result.length());
		assertTrue(LONG_TEXT_BASE_32.equals(result));

		result = Base32Util.toBase32(SHORT_TEXT);
		assertEquals(SHORT_TEXT_BASE_32.length(), result.length());
		assertTrue(SHORT_TEXT_BASE_32.equals(result));

		for (int i = 0; i < WORDS.length; i++) {
			result = Base32Util.toBase32(WORDS[i]);
			assertEquals(WORDS_BASE_32[i].length(), result.length());
			assertTrue(WORDS_BASE_32[i].equals(result));
		}
	}

	@Test
	public void testFromBase32() {
		String result = Base32Util.fromBase32AsString(LONG_TEXT_BASE_32);
		assertEquals(LONG_TEXT.length(), result.length());
		assertTrue(LONG_TEXT.equals(result));

		result = Base32Util.fromBase32AsString(SHORT_TEXT_BASE_32);
		assertEquals(SHORT_TEXT.length(), result.length());
		assertTrue(SHORT_TEXT.equals(result));

		for (int i = 0; i < WORDS.length; i++) {
			result = Base32Util.fromBase32AsString(WORDS_BASE_32[i]);
			assertEquals(WORDS[i].length(), result.length());
			assertTrue(WORDS[i].equals(result));
		}

		int count = 0;
		for (int i = 0; i < INVALID_CHARS_BASE_32.length(); i++) {
			try {
				Base32Util.fromBase32AsString(SHORT_TEXT_BASE_32 + INVALID_CHARS_BASE_32.charAt(i));
			} catch (Base32UtilException e) {
				count++;
			}
		}
		assertEquals(INVALID_CHARS_BASE_32.length(), count);

	}

	@Test
	public void testToBase32Hex() {
		String result = Base32Util.toBase32Hex(LONG_TEXT);
		assertTrue(LONG_TEXT_BASE_32_HEX.equals(result));
		assertEquals(LONG_TEXT_BASE_32_HEX.length(), result.length());

		result = Base32Util.toBase32Hex(SHORT_TEXT);
		assertTrue(SHORT_TEXT_BASE_32_HEX.equals(result));
		assertEquals(SHORT_TEXT_BASE_32_HEX.length(), result.length());

		for (int i = 0; i < WORDS.length; i++) {
			result = Base32Util.toBase32Hex(WORDS[i]);
			assertEquals(WORDS_BASE_32_HEX[i].length(), result.length());
			assertTrue(WORDS_BASE_32_HEX[i].equals(result));
		}
	}

	@Test
	public void testFromBase32Hex() {
		String result = Base32Util.fromBase32HexAsString(LONG_TEXT_BASE_32_HEX);
		assertTrue(LONG_TEXT.equals(result));
		assertEquals(LONG_TEXT.length(), result.length());

		result = Base32Util.fromBase32HexAsString(SHORT_TEXT_BASE_32_HEX);
		assertEquals(SHORT_TEXT.length(), result.length());
		assertTrue(SHORT_TEXT.equals(result));

		for (int i = 0; i < WORDS.length; i++) {
			result = Base32Util.fromBase32HexAsString(WORDS_BASE_32_HEX[i]);
			assertEquals(WORDS[i].length(), result.length());
			assertTrue(WORDS[i].equals(result));
		}

		int count = 0;
		for (int i = 0; i < INVALID_CHARS_BASE_32_HEX.length(); i++) {
			try {
				Base32Util.fromBase32AsString(SHORT_TEXT_BASE_32_HEX + INVALID_CHARS_BASE_32_HEX.charAt(i));
			} catch (Base32UtilException e) {
				count++;
			}
		}
		assertEquals(INVALID_CHARS_BASE_32_HEX.length(), count);
	}

	@Test
	public void testToBase32Z() {
		String result = Base32Util.toBase32Z(LONG_TEXT);
		assertTrue(LONG_TEXT_BASE_32_Z.equals(result));
		assertEquals(LONG_TEXT_BASE_32_Z.length(), result.length());

		result = Base32Util.toBase32Z(SHORT_TEXT);
		assertEquals(SHORT_TEXT_BASE_32_Z.length(), result.length());
		assertTrue(SHORT_TEXT_BASE_32_Z.equals(result));

		for (int i = 0; i < WORDS.length; i++) {
			result = Base32Util.toBase32Z(WORDS[i]);
			assertEquals(WORDS_BASE_32_Z[i].length(), result.length());
			assertTrue(WORDS_BASE_32_Z[i].equals(result));
		}
	}

	@Test
	public void testFromBase32Z() {
		String result = Base32Util.fromBase32ZAsString(LONG_TEXT_BASE_32_Z);
		assertTrue(LONG_TEXT.equals(result));
		assertEquals(LONG_TEXT.length(), result.length());

		result = Base32Util.fromBase32ZAsString(SHORT_TEXT_BASE_32_Z);
		assertTrue(SHORT_TEXT.equals(result));
		assertEquals(SHORT_TEXT.length(), result.length());

		for (int i = 0; i < WORDS.length; i++) {
			result = Base32Util.fromBase32ZAsString(WORDS_BASE_32_Z[i]);
			assertEquals(WORDS[i].length(), result.length());
			assertTrue(WORDS[i].equals(result));
		}

		int count = 0;
		for (int i = 0; i < INVALID_CHARS_BASE_32_Z.length(); i++) {
			try {
				Base32Util.fromBase32AsString(SHORT_TEXT_BASE_32_Z + INVALID_CHARS_BASE_32_Z.charAt(i));
			} catch (Base32UtilException e) {
				count++;
			}
		}
		assertEquals(INVALID_CHARS_BASE_32_Z.length(), count);
	}

	@Test
	public void testToBase32Crockford() {
		String result = Base32Util.toBase32Crockford(LONG_TEXT);
		assertEquals(LONG_TEXT_BASE_32_CROCKFORD.length(), result.length());
		assertTrue(LONG_TEXT_BASE_32_CROCKFORD.equals(result));

		result = Base32Util.toBase32Crockford(SHORT_TEXT);
		assertEquals(SHORT_TEXT_BASE_32_CROCKFORD.length(), result.length());
		assertTrue(SHORT_TEXT_BASE_32_CROCKFORD.equals(result));

		for (int i = 0; i < WORDS.length; i++) {
			result = Base32Util.toBase32Crockford(WORDS[i]);
			assertEquals(WORDS_BASE_32_CROCKFORD[i].length(), result.length());
			assertTrue(WORDS_BASE_32_CROCKFORD[i].equals(result));
		}
	}

	@Test
	public void testFromBase32Crockford() {
		String result = Base32Util.fromBase32CrockfordAsString(LONG_TEXT_BASE_32_CROCKFORD);
		assertEquals(LONG_TEXT.length(), result.length());
		assertTrue(LONG_TEXT.equals(result));

		result = Base32Util.fromBase32CrockfordAsString(SHORT_TEXT_BASE_32_CROCKFORD);
		assertEquals(SHORT_TEXT.length(), result.length());
		assertTrue(SHORT_TEXT.equals(result));

		for (int i = 0; i < WORDS.length; i++) {
			result = Base32Util.fromBase32CrockfordAsString(WORDS_BASE_32_CROCKFORD[i]);
			assertEquals(WORDS[i].length(), result.length());
			assertTrue(WORDS[i].equals(result));
		}

		int count = 0;
		for (int i = 0; i < INVALID_CHARS_BASE_32_CROCKFORD.length(); i++) {
			try {
				Base32Util.fromBase32AsString(SHORT_TEXT_BASE_32_CROCKFORD + INVALID_CHARS_BASE_32_CROCKFORD.charAt(i));
			} catch (Base32UtilException e) {
				count++;
			}
		}
		assertEquals(INVALID_CHARS_BASE_32_CROCKFORD.length(), count);
	}

	@Test
	public void testToBase32AsNumber() {
		String result = null;

		// Encode from long to base 32
		for (int i = 0; i < NUMBERS.length; i++) {
			result = Base32Util.toBase32(NUMBERS[i]);
			assertEquals(NUMBERS_BASE_32[i].length(), result.length());
			assertEquals(NUMBERS_BASE_32[i], result);
		}

		// Encode from BigInteger to base 32
		for (int i = 0; i < NUMBERS.length; i++) {
			result = Base32Util.toBase32(BigInteger.valueOf((NUMBERS[i])));
			assertEquals(NUMBERS_BASE_32[i].length(), result.length());
			assertEquals(NUMBERS_BASE_32[i], result);
		}

		// Decode from base 32 to long
		long number = 0;
		for (int i = 0; i < NUMBERS.length; i++) {
			number = Base32Util.fromBase32AsLong((NUMBERS_BASE_32[i]));
			assertEquals(NUMBERS[i], number);
		}

		// Decode from base 32 to BigInteger
		for (int i = 0; i < NUMBERS.length; i++) {
			number = Base32Util.fromBase32AsBigInteger((NUMBERS_BASE_32[i])).longValue();
			assertEquals(NUMBERS[i], number);
		}
	}

	@Test
	public void testToBase32HexAsNumber() {
		String result = null;

		// Encode from long to base 32
		for (int i = 0; i < NUMBERS_BASE_32_HEX.length; i++) {
			result = Base32Util.toBase32Hex(NUMBERS[i]);
			assertEquals(NUMBERS_BASE_32_HEX[i].length(), result.length());
			assertEquals(NUMBERS_BASE_32_HEX[i], result);
		}

		// Encode from BigInteger to base 32
		for (int i = 0; i < NUMBERS_BASE_32_HEX.length; i++) {
			result = Base32Util.toBase32Hex(BigInteger.valueOf(NUMBERS[i]));
			assertEquals(NUMBERS_BASE_32_HEX[i].length(), result.length());
			assertEquals(NUMBERS_BASE_32_HEX[i], result);
		}

		// Decode from base 32 to long
		long number = 0;
		for (int i = 0; i < NUMBERS.length; i++) {
			number = Base32Util.fromBase32HexAsLong((NUMBERS_BASE_32_HEX[i]));
			assertEquals(NUMBERS[i], number);
		}

		// Decode from base 32 to BigInteger
		for (int i = 0; i < NUMBERS.length; i++) {
			number = Base32Util.fromBase32HexAsBigInteger((NUMBERS_BASE_32_HEX[i])).longValue();
			assertEquals(NUMBERS[i], number);
		}
	}

	@Test
	public void testToBase32ZAsNumber() {
		String result = null;

		// Encode from long to base 32
		for (int i = 0; i < NUMBERS.length; i++) {
			result = Base32Util.toBase32Z(NUMBERS[i]);
			assertEquals(NUMBERS_BASE_32_Z[i].length(), result.length());
			assertEquals(NUMBERS_BASE_32_Z[i], result);
		}

		// Encode from BigInteger to base 32
		for (int i = 0; i < NUMBERS.length; i++) {
			result = Base32Util.toBase32Z(BigInteger.valueOf(NUMBERS[i]));
			assertEquals(NUMBERS_BASE_32_Z[i].length(), result.length());
			assertEquals(NUMBERS_BASE_32_Z[i], result);
		}

		// Decode from base 32 to long
		long number = 0;
		for (int i = 0; i < NUMBERS.length; i++) {
			number = Base32Util.fromBase32ZAsLong((NUMBERS_BASE_32_Z[i]));
			assertEquals(NUMBERS[i], number);
		}

		// Decode from base 32 to BigInteger
		for (int i = 0; i < NUMBERS.length; i++) {
			number = Base32Util.fromBase32ZAsBigInteger((NUMBERS_BASE_32_Z[i])).longValue();
			assertEquals(NUMBERS[i], number);
		}
	}

	@Test
	public void testToBase32CrockfordAsNumber() {
		String result = null;

		// Encode from long to base 32
		for (int i = 0; i < NUMBERS.length; i++) {
			result = Base32Util.toBase32Crockford(NUMBERS[i]);
			assertEquals(NUMBERS_BASE_32_CROCKFORD[i].length(), result.length());
			assertEquals(NUMBERS_BASE_32_CROCKFORD[i], result);
		}

		// Encode from BigInteger to base 32
		for (int i = 0; i < NUMBERS.length; i++) {
			result = Base32Util.toBase32Crockford(BigInteger.valueOf(NUMBERS[i]));
			assertEquals(NUMBERS_BASE_32_CROCKFORD[i].length(), result.length());
			assertEquals(NUMBERS_BASE_32_CROCKFORD[i], result);
		}

		// Decode from base 32 to long
		long number = 0;
		for (int i = 0; i < NUMBERS.length; i++) {
			number = Base32Util.fromBase32CrockfordAsLong((NUMBERS_BASE_32_CROCKFORD[i]));
			assertEquals(NUMBERS[i], number);
		}

		// Decode from base 32 to BigInteger
		for (int i = 0; i < NUMBERS.length; i++) {
			number = Base32Util.fromBase32CrockfordAsBigInteger((NUMBERS_BASE_32_CROCKFORD[i])).longValue();
			assertEquals(NUMBERS[i], number);
		}
	}
}

package com.art.sell.util;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.web.client.RestTemplate;

import java.beans.PropertyDescriptor;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 账户中心单点登陆验证
 * @author bin
 * @date 2018/6/15 17:28
 */
public class SsoUtil {

    static final String SSOURL = "/user/user/login/sso";
    static final String SIGNKEY = "1223@#$!njikAuI9";
    static final String DS8_CUSTOMER = "ds8_customer";
    static final String DS8_ADMIN = "ds8_admin";
    static final String CHARSET = "utf-8";
    static final String SIGNTYPE = "SHA256";
    private RestTemplate restTemplate = new RestTemplate();

    public static void main(String[] args) {
        System.out.println(new SsoUtil().getTokenFromAccountCenter("17602134375", DS8_CUSTOMER, "https://www.ccbone.com"));
    }

    public String getTokenFromAccountCenter(String mobileNumber, String userAppCode, String accountUrl) {
        SimpleDateFormat sdf =new SimpleDateFormat("yyyyMMddHHmmss");
        SsoInVo request = new SsoInVo();
        request.setUserAppCode(userAppCode);
        request.setMobileNumber(mobileNumber);
        request.setSignType(SIGNTYPE);
        request.setSsoApplyDate(sdf.format(new Date()));
        SignUtils utils = new SignUtils();
        request.setSignature(utils.sha256Sign(request, SIGNKEY, CHARSET));
        String jsonStr = restTemplate.postForObject(accountUrl + SSOURL, request, String.class);
        return jsonStr;
    }

    class SsoInVo {


        public String getUserAppCode() {
            return userAppCode;
        }

        public void setUserAppCode(String userAppCode) {
            this.userAppCode = userAppCode;
        }

        public String getSsoApplyDate() {
            return ssoApplyDate;
        }

        public void setSsoApplyDate(String ssoApplyDate) {
            this.ssoApplyDate = ssoApplyDate;
        }


        public String getSignType() {
            return signType;
        }

        public void setSignType(String signType) {
            this.signType = signType;
        }

        public String getSignature() {
            return signature;
        }

        public void setSignature(String signature) {
            this.signature = signature;
        }

        public String getMobileNumber() {
            return mobileNumber;
        }

        public void setMobileNumber(String mobileNumber) {
            this.mobileNumber = mobileNumber;
        }


        /**
         * 用户应用编号 user_app_code
         */
        private String userAppCode;


        /**
         * 用户手机号
         */
        private String mobileNumber;

        /**
         * sso申请日期(格式为yyyyMMddHHmmss)
         */
        private String ssoApplyDate;


        /**
         * 签名算法
         */
        private String signType;

        /**
         * 请求签名
         */
        private String signature;

    }

    class LoginOutVo {


        public String getTicket() {
            return ticket;
        }

        public void setTicket(String ticket) {
            this.ticket = ticket;
        }

        public String getUserCode() {
            return userCode;
        }

        public void setUserCode(String userCode) {
            this.userCode = userCode;
        }

        /**
         * 登陆票据
         */
        private String ticket;

        /**
         * 用户编号
         */
        private String userCode;

    }


    class SignUtils {

        public String getSignCheckContent(Map<String, String> params) {
           return SsoUtil.getSignCheckContent(params);
        }


        public boolean sha256Verify(Map<String, String> params, String signKey, String signature, String charset) {
            return signature.equals(sha256Sign(params, signKey, charset));
        }

        public boolean sha256Verify(Object object, String signKey, String signature, String charset) {
            return signature.equals(sha256Sign(object, signKey, charset));
        }

        public String sha256Sign(String signKey, String charset, Map<String, String> params) {
            params.put("sign_key", signKey);
            String content = getSignCheckContent(params);
            byte[] b;
            try {
                b = CodecTools.sha256(content.getBytes(charset));
                return CodecTools.toHexString(b);
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException(e);
            }
        }

        public String sha256Sign(Map<String, Object> map, String signKey, String charset) {
            Map<String, String> params = new HashMap<String, String>();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
            for (Object o : map.keySet()) {
                Object v = map.get(o);
                if (v != null) {
                    String s = null;
                    if (v instanceof BigDecimal) {
                        s = format((BigDecimal) v);
                    } else if (v instanceof Date) {
                        s = sdf.format((Date) v);
                    }  else if (v instanceof String) {
                        s = (String) v;
                    } else {
                        s = v.toString();
                    }
                    params.put(o.toString(),s);
                }
            }



            return sha256Sign(signKey, charset, params);
        }


        public String sha256Sign(Object object, String signKey, String charset) {
            Map<String, Object> map = DomainTools.toMap(object);
            return sha256Sign(map, signKey, charset);
        }

        public String sha256Sign(Object object, String signKey, String charset, List<String> fieldList) {
            Map<String, Object> map = DomainTools.toMap(object);
            Map<String, Object> params = new HashMap<String, Object>();
            for (String str:fieldList){
                if (!map.containsKey(str)) {
                    throw new RuntimeException("Object has no field named " + str);
                }
                params.put(str, map.get(str));
            }

            return sha256Sign(params, signKey, charset);
        }


        private final DecimalFormat f = new DecimalFormat("#.#######");

        public String format(BigDecimal input) {
            return f.format(input);
        }

    }

    static class CodecTools {

        /**
         * 二进制数组转换成16进制的字符串
         *
         * @param b
         * @return
         */
        public static String toHexString(byte[] b) {
            return Hex.encodeHexString(b);
        }

        /**
         * 16进制的字符串转换成2进制数组
         *
         * @param str
         * @return
         */
        public static byte[] toByteFromHexString(String str) {
            try {
                return Hex.decodeHex(str.toCharArray());
            } catch (DecoderException e) {
                throw new RuntimeException(e);
            }
        }

        public static byte[] sha256(byte[] data) {
            return DigestUtils.sha256(data);
        }

        public static byte[] md5(byte[] data) {
            return DigestUtils.md5(data);
        }


    }


    static class DomainTools {

        /**
         * 将来源数据里的同名field拷贝给目标对象(浅拷贝,简单对象)
         *
         * @param source
         * 来源数据
         * @param clazz
         * 目标对象的class
         * @return
         * @throws IllegalAccessException
         * @throws InstantiationException
         */
        static ThreadLocal<StringBuilder> threadLocalStringBuilder = new ThreadLocal<StringBuilder>() {
            protected StringBuilder initialValue() {
                return new StringBuilder();
            }
        };

        public static <T> T copy(Object source, Class<T> clazz) {
            T target;
            try {
                target = clazz.newInstance();
            } catch (InstantiationException | IllegalAccessException ex) {
                throw new RuntimeException();
            }
            copy(source, target);

            return target;
        }

        /**
         * 对象转map
         *
         * @param object
         * @return
         * @throws Exception
         */
        public static Map<String, Object> toMap(Object object) {
            Map<String, Object> map = new HashMap<String, Object>();
            SsoInVo ssoInVo = (SsoInVo)object;
            map.put("userAppCode",ssoInVo.getUserAppCode());

            map.put("mobileNumber",ssoInVo.getMobileNumber());
            map.put("ssoApplyDate",ssoInVo.getSsoApplyDate());
            map.put("signType",ssoInVo.getSignType());
            map.put("signature",ssoInVo.getSignature());

            /*try {
                Class<?> clazz = object.getClass();
                Field[] fields = clazz.getDeclaredFields();


                for (Field field : fields) {
                    String fieldName = field.getName();
                    if (fieldName.equalsIgnoreCase("serialVersionUID")) {
                        continue;
                    }
                    PropertyDescriptor pd = new PropertyDescriptor(fieldName, clazz);
                    Method reader = pd.getReadMethod();
                    if (reader == null) {
                        continue;
                    }
                    Object value = reader.invoke(object, new Object[]{});

                    map.put(fieldName, value);
                }
                return map;
            } catch (Exception e) {
                throw new RuntimeException(e);
            }*/
            return map;
        }

        /**
         * 构造签名用的字符串
         * 1.先将object转为map
         * 2.再按照key升序排序
         * 3.再按照key获取value并将key和value拼接：key1value1key2value2.。。。。。。。
         *
         * @param object
         * @return 构造完成的签名字符串，按照key升序排列，key1value1key2value2.。。。。。。。keynvaluen
         * @throws Exception
         */
        public static String buildSignString(Object object) throws Exception {
            Map<String, Object> map = toMap(object);
            List<String> keyList = new ArrayList<String>();
            for (Object o :map.keySet()){
                keyList.add(o.toString());
            }

            Collections.sort(keyList);
            List<String> list = new ArrayList<String>();
            for (String str:keyList){
                Object value = map.get(str);
                if (value != null) {
                    list.add(str);
                    list.add(value.toString());
                }
            }

            StringBuilder sb = threadLocalStringBuilder.get();
            sb.delete(0, sb.length());
            for (Object o : list) {
                sb.append(o);
            }
            return sb.toString();
        }


        /**
         * map转对象
         *
         * @param map
         * @param clazz
         * @return
         * @throws Exception
         */
        public static <T> T copy(Map<String, Object> map, Class<T> clazz) {
            Field[] fields = clazz.getDeclaredFields();

            T t;
            try {
                t = clazz.newInstance();
            } catch (InstantiationException | IllegalAccessException ex) {
                throw new RuntimeException(ex);
            }

            for (Field field : fields) {
                String fieldName = field.getName();
                try {
                    Object value = map.get(fieldName);

                    if (value == null) {
                        continue;
                    }
                    PropertyDescriptor pd = new PropertyDescriptor(fieldName, clazz);
                    Method writer = pd.getWriteMethod();
                    if (writer == null) {
                        continue;
                    }
                    writer.invoke(t, new Object[]{value});

                } catch (Exception e) {

                }
            }

            return t;

        }

        /**
         * 将来源数据里的同名field拷贝给目标对象(浅拷贝,简单对象)
         *
         * @param source
         * @param target
         */
        public static void copy(Object source, Object target) {
            Class<?> sourceClass = source.getClass();
            Class<?> targetClass = target.getClass();

            Field[] sourceFileds = sourceClass.getDeclaredFields();
            Field[] fields = targetClass.getDeclaredFields();
            if (fields.length > sourceFileds.length) {
                fields = sourceFileds;
            }

            for (Field field : fields) {
                String fieldName = field.getName();
                try {
                    PropertyDescriptor pd = new PropertyDescriptor(fieldName, sourceClass);
                    Method reader = pd.getReadMethod();
                    if (reader == null){
                        continue;
                    }
                    pd = new PropertyDescriptor(fieldName, targetClass);
                    Method writer = pd.getWriteMethod();
                    if (writer == null) {
                        continue;
                    }
                    Object value = reader.invoke(source, new Object[]{});
                    if (value == null) {
                        continue;
                    }
                    writer.invoke(target, new Object[]{value});

                } catch (Exception e) {

                }
            }
        }

        /**
         * 集合复制(浅拷贝,简单对象)
         *
         * @param sources 源对象集合
         * @param targets 目标对象的空集合 size()=0
         * @param clazz   目标对象类型
         */
        public static <K, V> void copy(Collection<K> sources, Collection<V> targets, Class<V> clazz) {

            targets.clear();

            for (K source : sources) {

                V target;
                try {
                    target = clazz.newInstance();
                    copy(source, target);
                    targets.add(target);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }

            }

        }

        /**
         * 集合复制(浅拷贝,简单对象)
         *
         * @param sources 源对象集合
         * @param clazz   目标对象类型
         * @return 拷贝后的目标对象集合
         */
        public static <K, V> List<V> copy(Collection<K> sources, Class<V> clazz) {
            int size = sources == null ? 1 : sources.size();
            List<V> target = new ArrayList<V>(size);
            copy(sources, target, clazz);
            return target;
        }
    }

    public static String getSignCheckContent(Map<String, String> params) {
        if (params == null) {
            return null;
        }

        params.remove("sign");
        params.remove("signature");
        params.remove("sign_type");
        params.remove("signType");

        StringBuffer content = new StringBuffer();
        List<String> keys = new ArrayList<String>(params.keySet());
        Collections.sort(keys);

        for (int i = 0; i < keys.size(); i++) {
            String key = keys.get(i);
            String value = params.get(key);
            content.append((i == 0 ? "" : "&") + key + "=" + value);
        }

        return content.toString();
    }

    public static String sha256Sign(String signKey,String charset ,Map<String, String> params){
        params.put("sign_key", signKey);
        String content = getSignCheckContent(params);
        byte[] b;
        try {
            b = CodecTools.sha256(content.getBytes(charset));
            return CodecTools.toHexString(b);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }
}
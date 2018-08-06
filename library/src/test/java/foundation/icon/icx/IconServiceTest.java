package foundation.icon.icx;

import foundation.icon.icx.IcxCall.Builder;
import foundation.icon.icx.data.Address;
import foundation.icon.icx.data.NetworkId;
import foundation.icon.icx.transport.jsonrpc.*;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import static foundation.icon.icx.SampleKeys.PRIVATE_KEY_STRING;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class IconServiceTest {
    @Test
    void testIconServiceInit() {
        IconService iconService = new IconService(new Provider() {
            @Override
            public <O> Call<O> request(Request request, RpcConverter<O> converter) {
                return null;
            }

        });
        assertNotNull(iconService);
    }

    @SuppressWarnings("unused")
    @Test
    void testQuery() {
        Provider provider = mock(Provider.class);


        IconService iconService = new IconService(provider);
        iconService.addConverterFactory(new RpcConverter.RpcConverterFactory() {
            @Override
            public <T> RpcConverter<T> create(Class<T> type) {
                if(PersonResponse.class == type){
                    return new RpcConverter<T>() {
                        @Override
                        public T convertTo(RpcItem object) {
                            return null;
                        }

                        @Override
                        public RpcItem convertFrom(T object) {
                            return null;
                        }
                    };
                }
                return null;
            }
        });

        Person person = new Person();
        person.name = "gold bug";
        person.age = new BigInteger("20");
        person.hasPermission = false;

        IcxCall<PersonResponse> icxCall = new Builder()
                .from(Address.of("hxbe258ceb872e08851f1f59694dac2558708ece11").build())
                .to(Address.of("hx5bfdb090f43a808005ffc27c25b213145e80b7cd").build())
                .method("addUser")
                .params(person)
                .buildWith(PersonResponse.class);

        Call<PersonResponse> query = iconService.query(icxCall);

        verify(provider).request(
                argThat(Objects::nonNull),
                argThat(Objects::nonNull));

    }

    @Test
    void testGetTotalSupply() {
        Provider provider = mock(Provider.class);

        IconService iconService = new IconService(provider);
        iconService.getTotalSupply();

        verify(provider).request(
                argThat(request -> isRequestMatches(request, "icx_getTotalSupply", null)),
                argThat(Objects::nonNull));
    }

    @Test
    void testGetBalance() {
        Provider provider = mock(Provider.class);

        Address address = Address.of("hx4873b94352c8c1f3b2f09aaeccea31ce9e90bd31").build();

        IconService iconService = new IconService(provider);
        iconService.getBalance(address);

        HashMap<String, RpcValue> params = new HashMap<>();
        params.put("address", new RpcValue(address));

        verify(provider).request(
                argThat(request -> isRequestMatches(request, "icx_getBalance", params)),
                argThat(Objects::nonNull));
    }

    @Test
    void testGetBlockByHeight() {
        Provider provider = mock(Provider.class);

        IconService iconService = new IconService(provider);
        iconService.getBlock(BigInteger.ONE);

        HashMap<String, RpcValue> params = new HashMap<>();
        params.put("height", new RpcValue(BigInteger.ONE));

        verify(provider).request(
                argThat(request -> isRequestMatches(request, "icx_getBlockByHeight", params)),
                argThat(Objects::nonNull));
    }

    @Test
    void testGetBlockByHash() {
        Provider provider = mock(Provider.class);

        RpcValue hash = new RpcValue("0x033f8d96045eb8301fd17cf078c28ae58a3ba329f6ada5cf128ee56dc2af26f7");

        IconService iconService = new IconService(provider);
        iconService.getBlock(hash.asBytes());

        HashMap<String, RpcValue> params = new HashMap<>();
        params.put("hash", new RpcValue(hash));

        verify(provider).request(
                argThat(request -> isRequestMatches(request, "icx_getBlockByHash", params)),
                argThat(Objects::nonNull));
    }

    @Test
    void testGetLastBlock() {
        Provider provider = mock(Provider.class);

        IconService iconService = new IconService(provider);
        iconService.getLastBlock();

        verify(provider).request(
                argThat(request -> isRequestMatches(request, "icx_getLastBlock", null)),
                argThat(Objects::nonNull));
    }

    @SuppressWarnings("unchecked")
    @Test
    void testGetScoreApi() {
        Provider provider = mock(Provider.class);

        Address address = Address.of("cx4873b94352c8c1f3b2f09aaeccea31ce9e90bd31").build();

        IconService iconService = new IconService(provider);
        iconService.getScoreApi(address);

        HashMap<String, RpcValue> params = new HashMap<>();
        params.put("address", new RpcValue(address));

        verify(provider).request(
                argThat(request -> isRequestMatches(request, "icx_getScoreApi", params)),
                argThat(Objects::nonNull));
    }

    @Test
    void testGetTransaction() {
        Provider provider = mock(Provider.class);

        RpcValue hash = new RpcValue("0x2600770376fbf291d3d445054d45ed15280dd33c2038931aace3f7ea2ab59dbc");

        IconService iconService = new IconService(provider);
        iconService.getTransaction(hash.asBytes());

        HashMap<String, RpcValue> params = new HashMap<>();
        params.put("txHash", new RpcValue(hash));

        verify(provider).request(
                argThat(request -> isRequestMatches(request, "icx_getTransactionByHash", params)),
                argThat(Objects::nonNull));
    }

    @Test
    void testGetTransactionResult() {
        Provider provider = mock(Provider.class);

        RpcValue hash = new RpcValue("0x2600770376fbf291d3d445054d45ed15280dd33c2038931aace3f7ea2ab59dbc");

        IconService iconService = new IconService(provider);
        iconService.getTransactionResult(hash.asBytes());

        HashMap<String, RpcValue> params = new HashMap<>();
        params.put("txHash", new RpcValue(hash));

        verify(provider).request(
                argThat(request -> isRequestMatches(request, "icx_getTransactionResult", params)),
                argThat(Objects::nonNull));
    }

    @Test
    void testSendIcxTransaction() {
        Provider provider = mock(Provider.class);
        Address fromAddress = Address.of("hxbe258ceb872e08851f1f59694dac2558708ece11").build();
        Address toAddress = Address.of("hx5bfdb090f43a808005ffc27c25b213145e80b7cd").build();

        Transaction transaction = TransactionBuilder.of(NetworkId.MAIN)
                .from(fromAddress)
                .to(toAddress)
                .value(new BigInteger("de0b6b3a7640000", 16))
                .stepLimit(new BigInteger("12345", 16))
                .timestamp(new BigInteger("563a6cf330136", 16))
                .nonce(new BigInteger("1"))
                .build();
        Wallet wallet = KeyWallet.load(PRIVATE_KEY_STRING);
        SignedTransaction signedTransaction = new SignedTransaction(transaction, wallet);

        IconService iconService = new IconService(provider);
        iconService.sendTransaction(signedTransaction);

        String expected = "xR6wKs+IA+7E91bT8966jFKlK5mayutXCvayuSMCrx9KB7670CsWa0B7LQzgsxU0GLXaovlAT2MLs1XuDiSaZQE=";
        verify(provider).request(
                argThat(request -> {
                    boolean isMethodMathces = request.getMethod().equals("icx_sendTransaction");
                    boolean isSignaureMatches = request.getParams().getItem("signature").asString().equals(expected);
                    return isMethodMathces && isSignaureMatches;
                }),
                argThat(Objects::nonNull));
    }

    @Test
    void testTransferTokenTransaction() {
        Provider provider = mock(Provider.class);

        Address fromAddress = Address.of("hxbe258ceb872e08851f1f59694dac2558708ece11").build();
        Address scoreAddress = Address.of("cx982aed605b065b50a2a639c1ea5710ef5a0501a9").build();
        Address toAddress = Address.of("hx5bfdb090f43a808005ffc27c25b213145e80b7cd").build();
        Address.of("hxbe258ceb872e08851f1f59694dac2558708ece11").build();

        RpcObject params = new RpcObject.Builder()
                .put("_to", new RpcValue(toAddress))
                .put("_value", new RpcValue(new BigInteger("1")))
                .build();

        Transaction transaction = TransactionBuilder.of(NetworkId.MAIN)
                .from(fromAddress)
                .to(scoreAddress)
                .stepLimit(new BigInteger("12345", 16))
                .timestamp(new BigInteger("563a6cf330136", 16))
                .nonce(new BigInteger("1"))
                .call("transfer")
                .params(params)
                .build();

        Wallet wallet = KeyWallet.load(PRIVATE_KEY_STRING);
        SignedTransaction signedTransaction = new SignedTransaction(transaction, wallet);

        IconService iconService = new IconService(provider);
        iconService.sendTransaction(signedTransaction);

        String expected = "ITpAdh3bUV4Xj0WQIPlfhv+ppA+K+LtXqaYMjnt8pMwV7QJwyZNQuhH2ljdGPR+31wG+GpKEdOEuqeYOwODBVwA=";
        verify(provider).request(
                argThat(request -> {
                    boolean isMethodMathces = request.getMethod().equals("icx_sendTransaction");
                    boolean isSignaureMatches = request.getParams().getItem("signature").asString().equals(expected);
                    return isMethodMathces && isSignaureMatches;
                }),
                argThat(Objects::nonNull));
    }

    @SuppressWarnings("unchecked")
    private boolean isRequestMatches(Request request, String method, Map<String, RpcValue> params) {

        boolean isMethodMatches = request.getMethod().equals(method);

        boolean isParamMatches = (request.getParams() == null && params == null);
        if (!isParamMatches && params.size() > 0) {
            Set<String> keys = params.keySet();
            for (String key : keys) {
                RpcValue value = ((RpcValue) (request.getParams()).getItem(key));
                isParamMatches = value.asString().equals(params.get(key).asString());
                if (!isParamMatches) break;
            }
        }
        return isMethodMatches && isParamMatches;
    }

    @SuppressWarnings("WeakerAccess")
    class Person {
        public String name;
        public BigInteger age;
        public boolean hasPermission;
    }

    @SuppressWarnings("unused")
    class PersonResponse {
        public boolean isOk;
        public String message;
    }
}

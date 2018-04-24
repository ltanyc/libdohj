/*
 * Copyright 2013 Google Inc.
 * Copyright 2014 Andreas Schildbach
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.libdohj.params;

import org.bitcoinj.core.Utils;
import org.spongycastle.util.encoders.Hex;

import static com.google.common.base.Preconditions.checkState;
import java.io.ByteArrayOutputStream;
import org.bitcoinj.core.AltcoinBlock;
import org.bitcoinj.core.Block;
import static org.bitcoinj.core.Coin.COIN;
import org.bitcoinj.core.NetworkParameters;
import org.bitcoinj.core.Transaction;
import org.bitcoinj.core.TransactionInput;
import org.bitcoinj.core.TransactionOutput;
import org.bitcoinj.params.MainNetParams;
import org.bitcoinj.script.Script;
import org.bitcoinj.script.ScriptOpCodes;

/**
 * Parameters for the NewYorkCoin main production network on which people trade
 * goods and services.
 */
public class NewYorkCoinMainNetParams extends AbstractNewYorkCoinParams {
    public static final int MAINNET_MAJORITY_WINDOW = MainNetParams.MAINNET_MAJORITY_WINDOW;
    public static final int MAINNET_MAJORITY_REJECT_BLOCK_OUTDATED = MainNetParams.MAINNET_MAJORITY_REJECT_BLOCK_OUTDATED;
    public static final int MAINNET_MAJORITY_ENFORCE_BLOCK_UPGRADE = MainNetParams.MAINNET_MAJORITY_ENFORCE_BLOCK_UPGRADE;

    public NewYorkCoinMainNetParams() {
        super();
        id = ID_NY_MAINNET;
        packetMagic = 0xc0c0c0c0;

        maxTarget = Utils.decodeCompactBits(0x1e0fffffL);
        port = 17020;
        addressHeader = 60;
        p2shHeader = 22;
        acceptableAddressCodes = new int[] { addressHeader, p2shHeader };
        dumpedPrivateKeyHeader = 188;

        this.genesisBlock = createGenesis(this);
        spendableCoinbaseDepth = 100; //FIXME
        subsidyDecreaseBlockCount = 840000; //FIXME

        String genesisHash = genesisBlock.getHashAsString();
        checkState(genesisHash.equals("5597f25c062a3038c7fd815fe46c67dedfcb3c839fbc8e01ed4044540d08fe48"));
        alertSigningKey = Hex.decode("4AFC6B9D279C647C67C250BC4C68F5E9BD714867D8D45B6E54FEEFFA6CB9074EE7CBF69EB8FF388304604C2E00661601502B42E3CF71E934B4A5EAB0D5B209BFD");

        majorityEnforceBlockUpgrade = MAINNET_MAJORITY_ENFORCE_BLOCK_UPGRADE;
        majorityRejectBlockOutdated = MAINNET_MAJORITY_REJECT_BLOCK_OUTDATED;
        majorityWindow = MAINNET_MAJORITY_WINDOW;

        dnsSeeds = new String[] {
            "dnsseed.newyorkcoin.money"
        };
        bip32HeaderPub = 0x0488B21E;
        bip32HeaderPriv = 0x0488ADE4;
    }

    private static AltcoinBlock createGenesis(NetworkParameters params) {
        AltcoinBlock genesisBlock = new AltcoinBlock(params, Block.BLOCK_VERSION_GENESIS);
        Transaction t = new Transaction(params);
        try {
            byte[] bytes = Hex.decode
                    ("04ffff001d0104404e592054696d65732030352f4f63742f32303131205374657665204a6f62732c204170706c65e280997320566973696f6e6172792c2044696573206174203536");
            t.addInput(new TransactionInput(params, t, bytes));
            ByteArrayOutputStream scriptPubKeyBytes = new ByteArrayOutputStream();
            Script.writeBytes(scriptPubKeyBytes, Hex.decode
                    ("4AFC6B9D279C647C67C250BC4C68F5E9BD714867D8D45B6E54FEEFFA6CB9074EE7CBF69EB8FF388304604C2E00661601502B42E3CF71E934B4A5EAB0D5B209BFD"));
            scriptPubKeyBytes.write(ScriptOpCodes.OP_CHECKSIG);
            t.addOutput(new TransactionOutput(params, t, COIN.multiply(907477), scriptPubKeyBytes.toByteArray()));
        } catch (Exception e) {
            // Cannot happen.
            throw new RuntimeException(e);
        }
        genesisBlock.addTransaction(t);
        genesisBlock.setTime(1394102925L);
        genesisBlock.setDifficultyTarget(0x1e0ffff0L);
        genesisBlock.setNonce(2482334);
        return genesisBlock;
    }

    private static NewYorkCoinMainNetParams instance;
    public static synchronized NewYorkCoinMainNetParams get() {
        if (instance == null) {
            instance = new NewYorkCoinMainNetParams();
        }
        return instance;
    }

    @Override
    public String getPaymentProtocolId() {
        return ID_NY_MAINNET;
    }

    @Override
    public boolean isTestNet() {
        return false;
    }
}

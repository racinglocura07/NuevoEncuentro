package coop.nuevoencuentro.nofuemagia.sync;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.ContentResolver;
import android.content.Context;
import android.os.Bundle;

import coop.nuevoencuentro.nofuemagia.R;

/**
 * Created by jlionti on 06/06/2016. No Fue Magia
 */
public class SyncUtils {

    public static void CreateSyncAccount(Context context) {

        String nombre = context.getString(R.string.app_name);
        String tipo = context.getString(R.string.auth_type);

        Account account = new Account(nombre, tipo);
        AccountManager accountManager = (AccountManager) context.getSystemService(Context.ACCOUNT_SERVICE);
        if (accountManager.addAccountExplicitly(account, null, null)) {
            Bundle params = new Bundle();
            params.putBoolean(ContentResolver.SYNC_EXTRAS_EXPEDITED, false);
            params.putBoolean(ContentResolver.SYNC_EXTRAS_DO_NOT_RETRY, false);
            params.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL, false);

            ContentResolver.setIsSyncable(account, context.getString(R.string.provider), 1);
            ContentResolver.setSyncAutomatically(account, context.getString(R.string.provider), true);
            ContentResolver.addPeriodicSync(account, context.getString(R.string.provider), params, (60 * 60) * 24);

            ContentResolver.requestSync(account, context.getString(R.string.provider), params);
        }

        //BORRRAR UNA VEZ QUE ANDE
        Bundle params = new Bundle();
        params.putBoolean(ContentResolver.SYNC_EXTRAS_EXPEDITED, true);
        params.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL, true);
        ContentResolver.requestSync(account, context.getString(R.string.provider), params);
    }

    public static Account getAccount(Context context) {
        String nombre = context.getString(R.string.app_name);
        String tipo = context.getString(R.string.auth_type);

        Account account = new Account(nombre, tipo);
        return account;
    }
}

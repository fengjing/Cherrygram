package uz.unnarsx.cherrygram.preferences;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.R;
import org.telegram.ui.ActionBar.ActionBar;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Cells.HeaderCell;
import org.telegram.ui.Cells.ShadowSectionCell;
import org.telegram.ui.Cells.TextSettingsCell;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.ui.Components.RecyclerListView;
import org.telegram.ui.Components.UndoView;

import uz.unnarsx.cherrygram.CherrygramConfig;
import uz.unnarsx.cherrygram.prefviews.TextCheckWithIconCell;

public class DrawerIconsPreferencesEntry extends BaseFragment {

    private int rowCount;
    private ListAdapter listAdapter;

    private int drawerHeaderRow;
    private int newGroupRow;
    private int newSecretChatRow;
    private int newChannelRow;
    private int contactsRow;
    private int callsRow;
    private int savedMessagesRow;
    private int archivedChatsRow;
    private int peopleNearbyRow;
    private int scanQrRow;
    private int cgPreferencesRow;

    private int newGroupIcon;
    private int newSecretIcon;
    private int newChannelIcon;
    private int contactsIcon;
    private int callsIcon;
    private int savedIcon;
    private int archiveIcon;
    private int settingsIcon;
    private int peopleNearbyIcon;
    private int scanQrIcon;

    private UndoView restartTooltip;

    @Override
    public boolean onFragmentCreate() {
        super.onFragmentCreate();
        updateRowsId(true);
        return true;
    }

    @Override
    public View createView(Context context) {
        actionBar.setBackButtonImage(R.drawable.ic_ab_back);
        
        actionBar.setTitle(LocaleController.getString("AP_DrawerButtonsCategory", R.string.AP_DrawerButtonsCategory));
        actionBar.setAllowOverlayTitle(false);

        if (AndroidUtilities.isTablet()) {
            actionBar.setOccupyStatusBar(false);
        }

        actionBar.setActionBarMenuOnItemClick(new ActionBar.ActionBarMenuOnItemClick() {
            @Override
            public void onItemClick(int id) {
                if (id == -1) {
                    finishFragment();
                }
            }
        });

        listAdapter = new ListAdapter(context);
        fragmentView = new FrameLayout(context);
        fragmentView.setBackgroundColor(Theme.getColor(Theme.key_windowBackgroundGray));
        FrameLayout frameLayout = (FrameLayout) fragmentView;

        RecyclerListView listView = new RecyclerListView(context);
        listView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        listView.setVerticalScrollBarEnabled(false);
        listView.setAdapter(listAdapter);

        if (listView.getItemAnimator() != null) {
            ((DefaultItemAnimator) listView.getItemAnimator()).setDelayAnimations(false);
        }

        frameLayout.addView(listView, LayoutHelper.createFrame(LayoutHelper.MATCH_PARENT, LayoutHelper.MATCH_PARENT));
        listView.setOnItemClickListener((view, position, x, y) -> {
            if (position == newGroupRow) {
                CherrygramConfig.INSTANCE.toggleCreateGroupDrawerButton();
                if (view instanceof TextCheckWithIconCell) {
                    ((TextCheckWithIconCell) view).setChecked(CherrygramConfig.INSTANCE.getCreateGroupDrawerButton());
                }
                parentLayout.rebuildAllFragmentViews(false, false);
            } else if (position == newSecretChatRow) {
                CherrygramConfig.INSTANCE.toggleSecretChatDrawerButton();
                if (view instanceof TextCheckWithIconCell) {
                    ((TextCheckWithIconCell) view).setChecked(CherrygramConfig.INSTANCE.getSecretChatDrawerButton());
                }
                parentLayout.rebuildAllFragmentViews(false, false);
            } else if (position == newChannelRow) {
                CherrygramConfig.INSTANCE.toggleCreateChannelDrawerButton();
                if (view instanceof TextCheckWithIconCell) {
                    ((TextCheckWithIconCell) view).setChecked(CherrygramConfig.INSTANCE.getCreateChannelDrawerButton());
                }
                parentLayout.rebuildAllFragmentViews(false, false);
            } else if (position == contactsRow) {
                CherrygramConfig.INSTANCE.toggleContactsDrawerButton();
                if (view instanceof TextCheckWithIconCell) {
                    ((TextCheckWithIconCell) view).setChecked(CherrygramConfig.INSTANCE.getContactsDrawerButton());
                }
                parentLayout.rebuildAllFragmentViews(false, false);
            } else if (position == callsRow) {
                CherrygramConfig.INSTANCE.toggleCallsDrawerButton();
                if (view instanceof TextCheckWithIconCell) {
                    ((TextCheckWithIconCell) view).setChecked(CherrygramConfig.INSTANCE.getCallsDrawerButton());
                }
                parentLayout.rebuildAllFragmentViews(false, false);
            } else if (position == savedMessagesRow) {
                CherrygramConfig.INSTANCE.toggleSavedMessagesDrawerButton();
                if (view instanceof TextCheckWithIconCell) {
                    ((TextCheckWithIconCell) view).setChecked(CherrygramConfig.INSTANCE.getSavedMessagesDrawerButton());
                }
                parentLayout.rebuildAllFragmentViews(false, false);
            } else if (position == archivedChatsRow) {
                CherrygramConfig.INSTANCE.toggleArchivedChatsDrawerButton();
                if (view instanceof TextCheckWithIconCell) {
                    ((TextCheckWithIconCell) view).setChecked(CherrygramConfig.INSTANCE.getArchivedChatsDrawerButton());
                }
                parentLayout.rebuildAllFragmentViews(false, false);
            } else if (position == peopleNearbyRow) {
                CherrygramConfig.INSTANCE.togglePeopleNearbyDrawerButton();
                if (view instanceof TextCheckWithIconCell) {
                    ((TextCheckWithIconCell) view).setChecked(CherrygramConfig.INSTANCE.getPeopleNearbyDrawerButton());
                }
                parentLayout.rebuildAllFragmentViews(false, false);
            } else if (position == scanQrRow) {
                CherrygramConfig.INSTANCE.toggleScanQRDrawerButton();
                if (view instanceof TextCheckWithIconCell) {
                    ((TextCheckWithIconCell) view).setChecked(CherrygramConfig.INSTANCE.getScanQRDrawerButton());
                }
                parentLayout.rebuildAllFragmentViews(false, false);
            } else if (position == cgPreferencesRow) {
                CherrygramConfig.INSTANCE.toggleCGPreferencesDrawerButton();
                if (view instanceof TextCheckWithIconCell) {
                    ((TextCheckWithIconCell) view).setChecked(CherrygramConfig.INSTANCE.getCGPreferencesDrawerButton());
                }
                parentLayout.rebuildAllFragmentViews(false, false);
            }
        });
        restartTooltip = new UndoView(context);
        /*restartTooltip.setInfoText(LocaleController.formatString("RestartRequired", R.string.RestartRequired));*/
        frameLayout.addView(restartTooltip, LayoutHelper.createFrame(LayoutHelper.MATCH_PARENT, LayoutHelper.WRAP_CONTENT, Gravity.BOTTOM | Gravity.LEFT, 8, 0, 8, 8));
        return fragmentView;
    }

    @SuppressLint("NotifyDataSetChanged")
    private void updateRowsId(boolean notify) {
        rowCount = 0;
    
        drawerHeaderRow = rowCount++;
        newGroupRow = rowCount++;
        newSecretChatRow = rowCount++;
        newChannelRow = rowCount++;
        contactsRow = rowCount++;
        callsRow = rowCount++;
        savedMessagesRow = rowCount++;
        archivedChatsRow = rowCount++;
        peopleNearbyRow = rowCount++;
        scanQrRow = rowCount++;
        cgPreferencesRow = rowCount++;

        if (listAdapter != null && notify) {
            listAdapter.notifyDataSetChanged();
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onResume() {
        super.onResume();
        if (listAdapter != null) {
            listAdapter.notifyDataSetChanged();
        }
    }

    private class ListAdapter extends RecyclerListView.SelectionAdapter {
        private final Context mContext;

        public ListAdapter(Context context) {
            mContext = context;
        }

        @Override
        public int getItemCount() {
            return rowCount;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            switch (CherrygramConfig.INSTANCE.getEventType()) {
                case 1:
                    newGroupIcon = R.drawable.msg_groups_ny;
                    newSecretIcon = R.drawable.msg_secret_ny;
                    newChannelIcon = R.drawable.msg_channel_ny;
                    contactsIcon = R.drawable.msg_contacts_ny;
                    callsIcon = R.drawable.msg_calls_ny;
                    savedIcon = R.drawable.msg_saved_ny;
                    settingsIcon = R.drawable.msg_settings_ny;
                    peopleNearbyIcon = R.drawable.msg_nearby_ny;
                    break;
                case 2:
                    newGroupIcon = R.drawable.msg_groups_14;
                    newSecretIcon = R.drawable.msg_secret_14;
                    newChannelIcon = R.drawable.msg_channel_14;
                    contactsIcon = R.drawable.msg_contacts_14;
                    callsIcon = R.drawable.msg_calls_14;
                    savedIcon = R.drawable.msg_saved_14;
                    settingsIcon = R.drawable.msg_settings_14;
                    peopleNearbyIcon = R.drawable.msg_secret_14;
                    break;
                case 3:
                    newGroupIcon = R.drawable.msg_groups_hw;
                    newSecretIcon = R.drawable.msg_secret_hw;
                    newChannelIcon = R.drawable.msg_channel_hw;
                    contactsIcon = R.drawable.msg_contacts_hw;
                    callsIcon = R.drawable.msg_calls_hw;
                    savedIcon = R.drawable.msg_saved_hw;
                    settingsIcon = R.drawable.msg_settings_hw;
                    peopleNearbyIcon = R.drawable.msg_secret_hw;
                    break;
                case 4:
                    newGroupIcon = R.drawable.menu_groups_cn;
                    newSecretIcon = R.drawable.menu_secret_cn;
                    newChannelIcon = R.drawable.menu_broadcast_cn;
                    contactsIcon = R.drawable.menu_contacts_cn;
                    callsIcon = R.drawable.menu_calls_cn;
                    savedIcon = R.drawable.menu_bookmarks_cn;
                    settingsIcon = R.drawable.menu_settings_cn;
                    peopleNearbyIcon = R.drawable.menu_nearby_cn;
                    break;
                default:
                    newGroupIcon = R.drawable.msg_groups;
                    newSecretIcon = R.drawable.msg_secret;
                    newChannelIcon = R.drawable.msg_channel;
                    contactsIcon = R.drawable.msg_contacts;
                    callsIcon = R.drawable.msg_calls;
                    savedIcon = R.drawable.msg_saved;
                    settingsIcon = R.drawable.msg_settings;
                    peopleNearbyIcon = R.drawable.msg_nearby;
                    break;
            }
            switch (holder.getItemViewType()) {
                case 1:
                    holder.itemView.setBackground(Theme.getThemedDrawable(mContext, R.drawable.greydivider, Theme.key_windowBackgroundGrayShadow));
                    break;
                case 2:
                    HeaderCell headerCell = (HeaderCell) holder.itemView;
                    if (position == drawerHeaderRow) {
                        headerCell.setText(LocaleController.getString("AP_DrawerButtonsCategory", R.string.AP_DrawerButtonsCategory));
                    }
                    break;
                case 3:
                    TextCheckWithIconCell textCheckWithIconCell = (TextCheckWithIconCell) holder.itemView;
                    textCheckWithIconCell.setEnabled(true, null);
                    if (position == newGroupRow) {
                        textCheckWithIconCell.setTextAndCheckAndIcon(LocaleController.getString("NewGroup", R.string.NewGroup), newGroupIcon, CherrygramConfig.INSTANCE.getCreateGroupDrawerButton(), true);
                    } else if (position == newSecretChatRow) {
                        textCheckWithIconCell.setTextAndCheckAndIcon(LocaleController.getString("NewSecretChat", R.string.NewSecretChat), newSecretIcon, CherrygramConfig.INSTANCE.getSecretChatDrawerButton(), true);
                    } else if (position == newChannelRow) {
                        textCheckWithIconCell.setTextAndCheckAndIcon(LocaleController.getString("NewChannel", R.string.NewChannel), newChannelIcon, CherrygramConfig.INSTANCE.getCreateChannelDrawerButton(), true);
                    } else if (position == contactsRow) {
                        textCheckWithIconCell.setTextAndCheckAndIcon(LocaleController.getString("Contacts", R.string.Contacts), contactsIcon, CherrygramConfig.INSTANCE.getContactsDrawerButton(), true);
                    } else if (position == callsRow) {
                        textCheckWithIconCell.setTextAndCheckAndIcon(LocaleController.getString("Calls", R.string.Calls), callsIcon, CherrygramConfig.INSTANCE.getCallsDrawerButton(), true);
                    } else if (position == savedMessagesRow) {
                        textCheckWithIconCell.setTextAndCheckAndIcon(LocaleController.getString("SavedMessages", R.string.SavedMessages), savedIcon, CherrygramConfig.INSTANCE.getSavedMessagesDrawerButton(), true);
                    } else if (position == archivedChatsRow) {
                        textCheckWithIconCell.setTextAndCheckAndIcon(LocaleController.getString("ArchivedChats", R.string.ArchivedChats), R.drawable.msg_archive, CherrygramConfig.INSTANCE.getArchivedChatsDrawerButton(), true);
                    } else if (position == peopleNearbyRow) {
                        textCheckWithIconCell.setTextAndCheckAndIcon(LocaleController.getString("PeopleNearby", R.string.PeopleNearby), peopleNearbyIcon, CherrygramConfig.INSTANCE.getPeopleNearbyDrawerButton(), true);
                    } else if (position == scanQrRow) {
                        textCheckWithIconCell.setTextAndCheckAndIcon(LocaleController.getString("AuthAnotherClient", R.string.AuthAnotherClient), R.drawable.msg_qrcode, CherrygramConfig.INSTANCE.getScanQRDrawerButton(), true);
                    } else if (position == cgPreferencesRow) {
                        textCheckWithIconCell.setTextAndCheckAndIcon(LocaleController.getString("CGP_AdvancedSettings", R.string.CGP_AdvancedSettings), R.drawable.msg_settings, CherrygramConfig.INSTANCE.getCGPreferencesDrawerButton(), true);
                    }
                    break;
            }
        }

        @Override
        public boolean isEnabled(RecyclerView.ViewHolder holder) {
            int type = holder.getItemViewType();
            return type == 3 || type == 7;
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view;
            switch (viewType) {
                case 2:
                    view = new HeaderCell(mContext);
                    view.setBackgroundColor(Theme.getColor(Theme.key_windowBackgroundWhite));
                    break;
                case 3:
                    view = new TextCheckWithIconCell(mContext);
                    view.setBackgroundColor(Theme.getColor(Theme.key_windowBackgroundWhite));
                    break;
                case 4:
                    view = new TextSettingsCell(mContext);
                    view.setBackgroundColor(Theme.getColor(Theme.key_windowBackgroundWhite));
                    break;
                default:
                    view = new ShadowSectionCell(mContext);
                    break;
            }
            view.setLayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT));
            return new RecyclerListView.Holder(view);
        }

        @Override
        public int getItemViewType(int position) {
            if (position == drawerHeaderRow) {
                return 2;
            } else if (position == newGroupRow || position == newSecretChatRow || position == newChannelRow ||
                       position == contactsRow || position == callsRow || position == savedMessagesRow ||
                       position == archivedChatsRow || position == peopleNearbyRow || position == scanQrRow || position == cgPreferencesRow) {
                return 3;
            }
            return 1;
        }
    }
}

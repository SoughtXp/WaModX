<div align="center">

<picture>
  <source media="(prefers-color-scheme: dark)" srcset="./assets/brand/title_dark.png">
  <source media="(prefers-color-scheme: light)" srcset="./assets/brand/title_light.png">
  <img src="./assets/brand/title_light.png" width="300" alt="WaModX">
</picture>

<br>

<picture>
  <source media="(prefers-color-scheme: dark)" srcset="./assets/brand/stars_dark.png">
  <source media="(prefers-color-scheme: light)" srcset="./assets/brand/stars_light.png">
  <img src="./assets/brand/stars_light.png" height="24" alt="An unofficial WhatsApp mod built against stubs.">
</picture>

<br>
<br>

<p>
  <img src="https://img.shields.io/badge/2.26.36.70-16A34A?style=flat&logo=whatsapp&logoColor=white">
  &nbsp;
  <img src="https://img.shields.io/badge/Unofficial-1a1a1a?style=flat">
  <br>
  <img src="https://img.shields.io/badge/license%20%7C%20MIT-1a1a1a?style=flat">
</p>

</div>

<p align="center">
  <b>WaModX</b> is an <b>unofficial WhatsApp mod</b> shipped as a <b>standalone APK</b>
  built against hand-written stubs — never against WhatsApp's real classes.
</p>

<p align="center"><b>Requirements</b></p>

<p align="left">
<img src="./assets/icons/gear.svg" width="16" alt=""> <b>Apktool</b> <code>3.0.3+</code><br>
<img src="./assets/icons/coffee.svg" width="16" alt=""> <b>JDK</b> <code>17+</code><br>
<img src="./assets/icons/android.svg" width="16" alt=""> <b>Android SDK</b> <code>build-tools</code><br>
<img src="./assets/icons/phone.svg" width="16" alt=""> <b>WhatsApp</b> <code>2.26.36.70</code>
</p>

<br>

<picture>
  <source media="(prefers-color-scheme: dark)" srcset="./assets/headings/features_dark.svg">
  <source media="(prefers-color-scheme: light)" srcset="./assets/headings/features_light.svg">
  <img src="./assets/headings/features_light.svg" height="26" alt="Features">
</picture>

<br>

<p>
<img src="./assets/icons/check-done.svg" width="18" alt="done"> <b>Anti Revoke Status</b><br>
<img src="./assets/icons/check-done.svg" width="18" alt="done"> <b>Anti Revoke Messages</b><br>
<img src="./assets/icons/check-done.svg" width="18" alt="done"> <b>Freeze Last Seen</b><br>
<img src="./assets/icons/check-done.svg" width="18" alt="done"> <b>Hide Status View</b><br>
<img src="./assets/icons/check-done.svg" width="18" alt="done"> <b>Hide Blue Ticks</b><br>
<img src="./assets/icons/check-done.svg" width="18" alt="done"> <b>Blue Ticks After Reply</b><br>
<img src="./assets/icons/check-done.svg" width="18" alt="done"> <b>Hide Typing / Recording Audio</b><br>
<img src="./assets/icons/check-done.svg" width="18" alt="done"> <b>Download Status / Copy Status Text</b><br>
<img src="./assets/icons/check-done.svg" width="18" alt="done"> <b>Signature Bypass</b><br>
<img src="./assets/icons/check-orange.svg" width="18" alt="partial"> <b>Bypass Anti Ban</b><br>
<img src="./assets/icons/check-pending.svg" width="18" alt="pending"> <b>Anti View Once</b><br>
<img src="./assets/icons/check-pending.svg" width="18" alt="pending"> <b>Design Mods</b>
</p>

<br>

<picture>
  <source media="(prefers-color-scheme: dark)" srcset="./assets/headings/map_dark.svg">
  <source media="(prefers-color-scheme: light)" srcset="./assets/headings/map_light.svg">
  <img src="./assets/headings/map_light.svg" height="26" alt="Map">
</picture>

<br>

<p>Where each hook lands in the host — one <code>invoke-static</code> per call site. Open a feature to see the real smali print and how it works.</p>

<p><img src="./assets/icons/inject.svg" width="16" alt=""> In every print the lines in green, flagged with <code>+</code> down the left margin, are WaModX's injected hook — the whole block, not just the <code>invoke-static</code> line. The unmarked lines are WhatsApp's own code, kept for context; the numbers beside them are the host method's own line numbers.</p>

<details>
<summary><img src="./assets/icons/freeze.svg" width="16" alt=""> <b>Freeze Last Seen</b> → <code>LX/17Q.A00</code>, <code>LX/1cv.A00</code></summary>

**`smali/X/17Q.smali`** · `LX/17Q;->A00(Landroid/os/Message;)V` · Lines 108–120

```diff
    108
    109      .line 10
    110      if-ne v0, v1, :cond_continue_normal
    111
    112      .line 11
+   113      :cond_check_freeze
+   114      invoke-static {}, Ldev/soughtxp/wamodx/core/LastSeenPrivacy;->freezeLastSeen()Z
+   115
+   116      .line 12
+   117      move-result v0
+   118
+   119      .line 13
+   120      if-eqz v0, :cond_continue_normal
```

**`smali_classes2/X/1cv.smali`** · `LX/1cv;->A00(LX/1cv;Z)V` · Lines 214–229

```diff
    214      .line 94
    215      return-void
    216  .end method
    217
    218  .method public static A00(LX/1cv;Z)V
    219      .locals 8
    220
+   221      invoke-static {}, Ldev/soughtxp/wamodx/core/LastSeenPrivacy;->freezeLastSeen()Z
+   222
+   223      move-result v0
+   224
+   225      if-eqz v0, :continue_normal
+   226
+   227      return-void
+   228
+   229      :continue_normal
```

> Presence / last-seen push paths. The `invoke-static` calls `LastSeenPrivacy.freezeLastSeen()` (reads the `freezeLastSeen` pref, no args, boolean); 17Q gates it in `:cond_check_freeze`, 1cv puts it first. A true result makes the host `return-void`, so the last-seen packet is never sent.

</details>

<details>
<summary><img src="./assets/icons/ticks.svg" width="16" alt=""> <b>Hide Blue Ticks</b> → <code>LX/18p.A0O</code>, <code>LX/18p.A0Y</code></summary>

**`smali/X/18p.smali`** · `LX/18p;->A0O(LX/1Ex;Z)V` · Lines 4392–4404

```diff
   4392      .line 2
   4393      .line 3
   4394      move-object/from16 v1, p1
   4395
+  4396      :try_start_A0o
+  4397      move-object/from16 v2, p0
+  4398      invoke-static {v2, v1}, Ldev/soughtxp/wamodx/core/ReadReceiptPrivacy;->onReadReceipt(Ljava/lang/Object;Ljava/lang/Object;)Z
+  4399      move-result v2
+  4400      if-eqz v2, :cond_A0o_pass
+  4401      return-void
+  4402      :cond_A0o_pass
+  4403      :try_end_A0o
+  4404      .catch Ljava/lang/Throwable; {:try_start_A0o .. :try_end_A0o} :catch_A0o
```

**`smali/X/18p.smali`** · `LX/18p;->A0Y(LX/1Ex;)Z` · Lines 8533–8545

```diff
   8533
   8534      .line 1
   8535      move-object/from16 v1, p1
   8536
+  8537      :try_start_A0y
+  8538      move-object/from16 v2, p0
+  8539      invoke-static {v2, v1}, Ldev/soughtxp/wamodx/core/ReadReceiptPrivacy;->onReadReceipt(Ljava/lang/Object;Ljava/lang/Object;)Z
+  8540      move-result v2
+  8541      if-eqz v2, :cond_A0y_pass
+  8542      const/4 v4, 0x0
+  8543      return v4
+  8544      :cond_A0y_pass
+  8545      :try_end_A0y
```

> Read-receipt emission paths (`1Ex` = FMessage). Inside `try/catch` the hook gets `(this, message)` and `ReadReceiptPrivacy.onReadReceipt` returns true to suppress. A0O then `return-void`s; A0Y returns `false` instead of its normal value.

</details>

<details>
<summary><img src="./assets/icons/reply.svg" width="16" alt=""> <b>Blue Ticks After Reply</b> → <code>SendE2EMessageJob.A0G</code></summary>

**`smali_classes5/com/whatsapp/jobqueue/job/SendE2EMessageJob.smali`** · `Lcom/whatsapp/jobqueue/job/SendE2EMessageJob;->A0G()V` · Lines 9730–9746

```diff
   9730  .method public A0G()V
   9731      .locals 34
   9732
   9733      .line 0
   9734      move-object/from16 v0, p0
   9735
+  9736      :try_start_reply
+  9737      invoke-static {v0}, Ldev/soughtxp/wamodx/core/ReadReceiptPrivacy;->onReply(Ljava/lang/Object;)V
+  9738      :try_end_reply
+  9739      .catch Ljava/lang/Throwable; {:try_start_reply .. :try_end_reply} :catch_reply
+  9740      :catch_reply
   9741
   9742      .line 1
   9743      .line 2
   9744      iget-object v3, v0, Lcom/whatsapp/jobqueue/job/SendE2EMessageJob;->id:Ljava/lang/String;
   9745
   9746      .line 3
```

> Runs when an outgoing E2E message is dispatched. The first statement (in `try/catch`) passes the job to `ReadReceiptPrivacy.onReply`, which caches the WaJobManager and notifies `BlueTickManager.onUserReply`. Returns void, does not stop the job (fire-and-continue).

</details>

<details>
<summary><img src="./assets/icons/hourglass.svg" width="16" alt=""> <b>Read Receipt Job</b> → <code>SendReadReceiptJob.A0G</code></summary>

**`smali_classes7/com/whatsapp/messaging/receipts/jobqueue/job/SendReadReceiptJob.smali`** · `Lcom/whatsapp/messaging/receipts/jobqueue/job/SendReadReceiptJob;->A0G()V` · Lines 628–646

```diff
    628  .method public A0G()V
    629      .locals 18
    630
    631      .line 0
    632      move-object/from16 v1, p0
    633
+   634      :try_start_bt
+   635      invoke-static {v1}, Ldev/soughtxp/wamodx/core/ReadReceiptPrivacy;->onReceiptJob(Ljava/lang/Object;)Z
+   636      move-result v0
+   637      if-eqz v0, :cond_bt_pass
+   638      return-void
+   639      :cond_bt_pass
+   640      :try_end_bt
+   641      .catch Ljava/lang/Throwable; {:try_start_bt .. :try_end_bt} :catch_bt
+   642      :catch_bt
    643
    644      .line 1
    645      .line 2
    646      invoke-direct {v1}, Lcom/whatsapp/messaging/receipts/jobqueue/job/SendReadReceiptJob;->A00()Ljava/lang/String;
```

> The background job that sends a read receipt. At the first instruction (in `try/catch`) the job goes to `ReadReceiptPrivacy.onReceiptJob`. A true result makes the host `return-void`, cancelling the receipt send entirely.

</details>

<details>
<summary><img src="./assets/icons/undo.svg" width="16" alt=""> <b>Anti Revoke Messages</b> → <code>LX/1DU.A00</code>, <code>LX/BOY.getDateView</code></summary>

**`smali/X/1DU.smali`** · `LX/1DU;->A00(LX/1DU;LX/1Ex;IZ)LX/7jd;` · Lines 768–783

```diff
    768  .method public static A00(LX/1DU;LX/1Ex;IZ)LX/7jd;
    769      .locals 40
    770
+   771      move-object/from16 v1, p1
+   772
+   773      const/4 v0, 0x0
+   774
+   775      invoke-static {v1, v0}, Ldev/soughtxp/wamodx/core/MessageRevokePrivacy;->antiRevoke(Ljava/lang/Object;Landroid/widget/TextView;)Z
+   776
+   777      move-result v0
+   778
+   779      if-eqz v0, :cond_antirevoke
+   780
+   781      sget-object v0, LX/8Xn;->A00:LX/8Xn;
+   782      const/4 v1, 0x1
+   783      new-instance v2, LX/7jd;
```

**`smali_classes7/X/BOY.smali`** · `LX/BOY;->getDateView()Landroid/widget/TextView;` · Lines 21476–21490

```diff
  21476      .line 14
  21477      move-result-object v0
  21478
  21479      iput-object v0, v2, LX/BOR;->A0F:Landroid/widget/TextView;
  21480
  21481      :cond_0
+ 21482      iget-object v1, p0, LX/BOW;->A0D:LX/1Ex;
+ 21483      invoke-static {v1, v0}, Ldev/soughtxp/wamodx/core/MessageRevokePrivacy;->antiRevoke(Ljava/lang/Object;Landroid/widget/TextView;)Z
  21484
  21485      return-object v0
  21486  .end method
  21487
  21488  .method public getDateWrapper()Landroid/view/ViewGroup;
  21489      .locals 2
  21490
```

> 1DU builds the result for a delete-for-everyone event; BOY builds a chat-row date TextView. In 1DU the hook gets `(message, null)`, stores the revoked id and returns true, so the host returns a placeholder `LX/7jd` (replace-return). In BOY it gets `(message, dateView)` and paints a red dot on revoked rows (fire-and-continue).

</details>

<details>
<summary><img src="./assets/icons/mic.svg" width="16" alt=""> <b>Hide Typing / Recording</b> → <code>LX/1im.CCQ</code>, <code>LX/6nE.A01</code></summary>

**`smali_classes2/X/1im.smali`** · `LX/1im;->CCQ(Landroid/os/Message;)V` · Lines 286–300

```diff
    286      .line 9
    287      .line 10
    288      if-ne v2, v0, :cond_continue
    289
    290      .line 11
    291      .line 12
+   292      :cond_check_hide
+   293      invoke-static {}, Ldev/soughtxp/wamodx/core/ComposingPrivacy;->hideComposing()Z
+   294
+   295      .line 13
+   296      .line 14
+   297      move-result v0
    298
    299      .line 15
    300      .line 16
```

**`smali_classes5/X/6nE.smali`** · `LX/6nE;->A01(LX/0Cw;LX/6nE;IZ)V` · Lines 363–378

```diff
    363      .line 86
    364      return-object p0
    365  .end method
    366
    367  .method public static A01(LX/0Cw;LX/6nE;IZ)V
    368      .locals 11
    369
+   370      invoke-static {}, Ldev/soughtxp/wamodx/core/ComposingPrivacy;->hideComposing()Z
+   371
+   372      move-result v0
+   373
+   374      if-eqz v0, :continue_normal
+   375
+   376      return-void
+   377
+   378      :continue_normal
```

> Composing / recording presence paths. The hook calls `ComposingPrivacy.hideComposing()` (reads the `hideComposing` pref) — gated in `:cond_check_hide` for 1im, first instruction for 6nE. A true result makes the host take an early `return-void`.

</details>

<details>
<summary><img src="./assets/icons/status.svg" width="16" alt=""> <b>Status Actions</b> → <code>StatusPlaybackContactFragment.A0K</code></summary>

**`smali_classes10/com/whatsapp/status/playback/fragment/StatusPlaybackContactFragment.smali`** · `Lcom/whatsapp/status/playback/fragment/StatusPlaybackContactFragment;->A0K(LX/LdP;LX/Lbv;...)V` · Lines 6841–6856

```diff
   6841      .line 310
   6842      iget-object v0, v2, LX/Khm;->A0G:Landroid/widget/TextView;
   6843
   6844      .line 311
   6845      .line 312
   6846      invoke-virtual {v0, v3}, Landroid/widget/TextView;->setText(Ljava/lang/CharSequence;)V
   6847
+  6848      invoke-static {p0, v0}, Ldev/soughtxp/wamodx/core/StatusHelper;->setupStatusActionDelayed(Ljava/lang/Object;Landroid/widget/TextView;)V
   6849
   6850      .line 313
   6851      .line 314
   6852      .line 315
   6853      goto :goto_4
   6854
   6855      .line 316
   6856      :cond_e
```

> Lays out a contact's status playback (caption TextView). Right after the caption `setText`, the hook gets `(fragment, captionTextView)` and `StatusHelper.setupStatusActionDelayed` returns void. Fire-and-continue: the mod attaches its download / copy-text actions.

</details>

<details>
<summary><img src="./assets/icons/sliders.svg" width="16" alt=""> <b>Settings Entry</b> → <code>SettingsTabActivity.onResume</code></summary>

**`smali_classes8/com/whatsapp/settings/ui/SettingsTabActivity.smali`** · `Lcom/whatsapp/settings/ui/SettingsTabActivity;->onResume()V` · Lines 15998–16012

```diff
  15998      .line 20
  15999      .line 21
  16000      .line 22
  16001      :cond_0
  16002      invoke-super {p0}, LX/0I8;->onResume()V
  16003
+ 16004      iget-object v0, p0, Lcom/whatsapp/settings/ui/SettingsTabActivity;->A10:Landroid/widget/LinearLayout;
+ 16005      invoke-static {v0}, Ldev/soughtxp/wamodx/core/SettingsHook;->inject(Landroid/view/ViewGroup;)V
  16006
  16007      .line 23
  16008      .line 24
  16009      .line 25
  16010      iget-object v0, p0, LX/0I8;->A03:LX/08a;
  16011
  16012      .line 26
```

> Runs when the Settings tab becomes visible. Right after `invoke-super onResume` the hook gets the `A10` LinearLayout and `SettingsHook.inject` returns void. Fire-and-continue: the mod inserts its own privacy row below the Privacy preference.

</details>

<details>
<summary><img src="./assets/icons/lock.svg" width="16" alt=""> <b>Signature Bypass / Bypass Anti Ban</b></summary>

<sub>Available in release builds. Its hook sites and mechanism aren't documented here.</sub>

</details>

<br>

<picture>
  <source media="(prefers-color-scheme: dark)" srcset="./assets/headings/contributing_dark.svg">
  <source media="(prefers-color-scheme: light)" srcset="./assets/headings/contributing_light.svg">
  <img src="./assets/headings/contributing_light.svg" height="26" alt="Contributing">
</picture>

<br>

The most useful PR you can send is a stub that stopped matching after a
WhatsApp update. One concern per PR keeps it reviewable, and easy to
revert on its own if it turns out to be wrong.

<p align="left">
<img src="./assets/icons/fork.svg" width="16" alt=""> <b>Fork</b> your own copy (<a href="https://github.com/SoughtXp/WaModX/fork">fork this repo</a>)<br>
<img src="./assets/icons/branch.svg" width="16" alt=""> <b>Branch</b> one per change<br>
<img src="./assets/icons/commit.svg" width="16" alt=""> <b>Commit</b> one concern per commit<br>
<img src="./assets/icons/push.svg" width="16" alt=""> <b>Push</b> to your fork<br>
<img src="./assets/icons/pull-request.svg" width="16" alt=""> <b>Pull Request</b> against <code>main</code> (<a href="../../pulls">open a PR</a>)
</p>

<br>

<picture>
  <source media="(prefers-color-scheme: dark)" srcset="./assets/headings/license_dark.svg">
  <source media="(prefers-color-scheme: light)" srcset="./assets/headings/license_light.svg">
  <img src="./assets/headings/license_light.svg" height="26" alt="License">
</picture>

<p>Released under the <a href="./LICENSE"><b>MIT License</b></a>.</p>

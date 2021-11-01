package com.vn.adssdk;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.sdk.ads.AdManager;
import com.vn.adssdk.databinding.FragmentFirstBinding;

import kotlin.Unit;

public class FirstFragment extends Fragment {

    private FragmentFirstBinding binding;

    @Override
    public View onCreateView(
        LayoutInflater inflater, ViewGroup container,
        Bundle savedInstanceState
    ) {

        binding = FragmentFirstBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.buttonFirst
            .setOnClickListener(view1 -> {
//                NavHostFragment.findNavController(FirstFragment.this).navigate(R.id.action_FirstFragment_to_SecondFragment);

                AdManager.showGGBanner(binding.adContainer);

                AdManager.showGGNative(binding.nativeAdView);

//                AdManager.loadGGInterstitial(getActivity(), interstitialAd -> {
//                    AdManager.showGGInterstitial(getActivity());
//                    return Unit.INSTANCE;
//                });

//                AdManager.loadGGRewarded(
//                    getActivity(),
//                    loadAdError -> Unit.INSTANCE,
//                    rewardedAd -> {
//                        AdManager.showGGRewarded(getActivity());
//                        return Unit.INSTANCE;
//                    }
//                );

            });

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}
package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.TransferDto;

import java.util.List;

public interface TransferDao {

    Transfer getTransferById(int transferId);

    List<Transfer> getTransfersFromAccount(int accountFrom, int userId);

    List<Transfer> getPendingTransfers(boolean isPending, int userId);

    List<Transfer> getApprovedTransfers(boolean isApproved, int userId);

    List<Transfer> getRejectedTransfers(boolean isRejected, int userId);

    Transfer createNewTransfer(Transfer transfer);

    int approveTransfer(int transferId);

    int rejectTransfer(int transferId);
}

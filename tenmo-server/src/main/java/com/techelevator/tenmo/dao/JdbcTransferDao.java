package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.exception.DaoException;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.TransferDto;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Component
public class JdbcTransferDao implements TransferDao {

    private final JdbcTemplate jdbcTemplate;

    private final JdbcUserDao userDao;

    public JdbcTransferDao(JdbcTemplate jdbcTemplate, JdbcUserDao userDao) {

        this.jdbcTemplate = jdbcTemplate;
        this.userDao = userDao;
    }

    private String TRANSFER_INSERT = "INSERT INTO public.transfers(\n" +
            "\tstatus_id, type_id, amount, account_to, account_from)\n" +
            "\tVALUES (?, ?, ?, ?, ?) RETURNING transfer_id";

    @Override
    public Transfer getTransferById(int transferId) {
        Transfer transfer = null;
        String sql = "SELECT transfers.transfer_id, transfers.status_id, transfers.type_id, transfers.amount, transfers.account_to, transfers.account_from, transfer_type.type_name, transfer_status.status_name " +
                "FROM transfers " +
                "JOIN transfer_type ON transfers.type_id = transfer_type.type_id " +
                "JOIN transfer_status ON transfers.status_id = transfer_status.status_id " +
                "WHERE transfers.transfer_id = ? ";
        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql, transferId);
            if (results.next()) {
                transfer = mapRowToTransfer(results);
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        }
        return transfer;
    }

    private List<Transfer> getTransfersByField(String field, Object value, int userId) {
        List<Transfer> transfers = new ArrayList<>();
        String sql = "SELECT transfer_id, transfers.status_id, status_name, transfers.type_id, type_name, amount, account_to, account_from " +
                "FROM transfers " +
                "INNER JOIN transfer_status ON transfers.status_id = transfer_status.status_id " +
                "INNER JOIN transfer_type ON transfer_type.type_id = transfers.type_id WHERE transfers." + field + " = ?";
        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql, value);
            while (results.next()) {
                transfers.add(mapRowToTransfer(results));
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        }
        return transfers;
    }

    @Transactional
    @Override
    public List<Transfer> getTransfersFromAccount(int accountFrom, int userId) {
        return getTransfersByField("account_from", accountFrom, userId);
    }

    @Override
    public List<Transfer> getPendingTransfers(boolean isPending, int userId) {
        return getTransfersByField("status_id", 1, userId);
    }

    @Override
    public List<Transfer> getApprovedTransfers(boolean isApproved, int userId) {
        return getTransfersByField("status_id", 2, userId);
    }

    @Override
    public List<Transfer> getRejectedTransfers(boolean isRejected, int userId) {
        return getTransfersByField("status_id", 3, userId);
    }

    @Override
    public Transfer createNewTransfer(Transfer transfer) {
        int newTransferId;
        try {
            newTransferId = jdbcTemplate.queryForObject(TRANSFER_INSERT, int.class,
                    transfer.getStatusId(), transfer.getTypeId(), transfer.getAmount(), transfer.getAccountTo(), transfer.getAccountFrom());

            return getTransferById(newTransferId);
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        } catch (Exception e) {
            throw new DaoException("An Error Occurred Saving this monster", e);
        }
    }

    @Override
    public int approveTransfer(int transferId) {
        try {
            return jdbcTemplate.update("UPDATE transfers SET status_id = 2 WHERE transfer_id = ? ", transferId);
        } catch (Exception e) {
            throw new DaoException("An Error Occurred Approving this transfer", e);
        }
    }

    @Override
    public int rejectTransfer(int transferId) {
        try {
            return jdbcTemplate.update("UPDATE transfers SET status_id = 3 WHERE transfer_id = ? ", transferId);
        } catch (Exception e) {
            throw new DaoException("An Error Occurred Rejecting this transfer", e);
        }
    }

    private Transfer mapRowToTransfer(SqlRowSet rs) {
        Transfer transfer = new Transfer();
        transfer.setTransferId(rs.getLong("transfer_id"));
        transfer.setTransferStatus(rs.getString("status_name"));
        transfer.setTransferType(rs.getString("type_name"));
        transfer.setAmount(rs.getBigDecimal("amount"));
        transfer.setAccountFrom(rs.getInt("account_from"));
        transfer.setAccountTo(rs.getInt("account_to"));
        transfer.setTypeId(rs.getInt("type_id"));
        transfer.setStatusId(rs.getInt("status_id"));

        return transfer;
    }
}

package com.example.messageservice.service.imp;

import com.example.messageservice.domain.entity.Account;
import com.example.messageservice.domain.entity.User;
import com.example.messageservice.exception.DuplicateException;
import com.example.messageservice.exception.InvalidException;
import com.example.messageservice.exception.NotFoundException;
import com.example.messageservice.input.RegisterInput;
import com.example.messageservice.input.ResetPasswordInput;
import com.example.messageservice.input.UpdateUserDetailInput;
import com.example.messageservice.output.FlagResponse;
import com.example.messageservice.repository.AccountRepository;
import com.example.messageservice.repository.UserRepository;
import com.example.messageservice.service.AccountService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AccountServiceImp implements AccountService {
  private final AccountRepository accountRepository;
  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;

  public AccountServiceImp(AccountRepository accountRepository, UserRepository userRepository, PasswordEncoder passwordEncoder) {
    this.accountRepository = accountRepository;
    this.userRepository = userRepository;
    this.passwordEncoder = passwordEncoder;
  }

  @Override
  public Account getUserByEmail(String email) {
    Account account = accountRepository.findByEmail(email);
    if (account == null) {
      throw new NotFoundException("Not found user by email = " + email);
    }
    return account;
  }

  @Override
  public List<Account> getAllUserByKey(Integer id, String key) {
    if (key.trim().equals("")) {
      return new ArrayList<>();
    }
    return accountRepository.findAllByEmailContaining(key).stream().filter(item -> !item.getId().equals(id)).collect(Collectors.toList());
  }

  @Override
  public Account login(String email, String password) {
    Account account = accountRepository.findByEmail(email);
    if (passwordEncoder.matches(password, account.getPassword())) {
      return account;
    }
    throw new InvalidException("Email and password is incorrect");
  }

  @Override
  public FlagResponse register(RegisterInput input) {
    if (accountRepository.findByEmail(input.getEmail()) != null) {
      throw new DuplicateException("Duplicate email " + input.getEmail());
    }
    try {
      // String email, String password, String state
      Account account = accountRepository.save(new Account(input.getEmail(), passwordEncoder.encode(input.getPassword()), input.getState()));
      // String name, String gender, Date dob, String phone, String email, String address, String job, String workplace, boolean isTopStudent, String feedback, String state
      User user = userRepository.save(new User(input.getName(), input.getGender(), input.getDob(), input.getPhone(),
          input.getEmail(), input.getAddress(), input.getJob(), input.getWorkplace(), input.isTopStudent(), input.getFeedback(), input.getState()));

      user.setAccount(account);
      account.setUser(user);

      accountRepository.save(account);
      userRepository.save(user);
    } catch (Exception ex) {
      return new FlagResponse(false);
    }

    return new FlagResponse(true);
  }

  @Override
  public FlagResponse resetPassword(ResetPasswordInput input) {
    try {
      Account account = accountRepository.findByEmail(input.getEmail());
      if (account == null) {
        throw new NotFoundException("Can not find account by email = " + input.getEmail());
      }

      account.setPassword(passwordEncoder.encode(input.getPassword()));
      accountRepository.save(account);
    } catch (Exception ex) {
      return new FlagResponse(false);
    }

    return new FlagResponse(true);
  }

  @Override
  public User getUserDetail(String email) {
    Account account = accountRepository.findByEmail(email);
    if (account == null) {
      throw new NotFoundException("Can not find user by email = " + email);
    }

    return account.getUser();
  }

  @Override
  public FlagResponse updateUserDetail(UpdateUserDetailInput input) {
    try {
      Account account = accountRepository.findByEmail(input.getEmail());
      User user = account.getUser();

      user.setName(input.getName());
      user.setGender(input.getGender());
      user.setDob(input.getDob());
      user.setPhone(input.getPhone());
      user.setAddress(input.getAddress());
      user.setJob(input.getJob());
      user.setWorkplace(input.getWorkplace());
      user.setTopStudent(input.isTopStudent());
      user.setFeedback(input.getFeedback());
      user.setState(input.getState());

      userRepository.save(user);
    } catch (Exception ex) {
      return new FlagResponse(false);
    }

    return new FlagResponse(true);
  }

}

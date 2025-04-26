import React, { useEffect } from 'react'
import { useState } from 'react';
import "../user-management/userManagement.css"
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faMagnifyingGlass, faXmark, faGear } from '@fortawesome/free-solid-svg-icons';
import { useNavigate } from 'react-router-dom';
import ReactPaginate from 'react-paginate';
import axiosInstance from '../../api/axiosInstance';
import useAuth from '../../hooks/useAuth';

export default function UserManagement() {

  const[isUserSearching, setIsUserSearching] = useState(false);
  const[toggleXMark, setToggleXMark] = useState(false);
  const[moderatorExists, setModeratorExists] = useState(true);
  const[userExists, setUserExists] = useState(true);
  const[isResult, setIsResult] = useState(false);
  const[resultId, setResultId] = useState(0);
  const[resultUsername, setResultUsername] = useState("");
  const[searchingWord, setSearchingWord] = useState("");
  const { auth } = useAuth();
  const isAdmin = auth?.roles?.includes("ADMIN");
  const navigate = useNavigate();

  const [sortDirection, setSortDirection] = useState("asc");
  const [selectedDirection, setSelectedDirection] = useState("Növekvő");
  const [isOpenDirectionSelector, setIsOpenDirectionSelector] = useState(false);
  const optionsSortingDirection = [
    { value: "growing", label: "Növekvő" },
    { value: "decreasing", label: "Csökkenő" },
  ];

  const handleSelectDirection = (option) => {
    setSelectedDirection(option.label);
    setIsOpenDirectionSelector(false);

    if (option.value === "growing") {
      setSortDirection("asc"); 
    } else if (option.value === "decreasing") {
      setSortDirection("desc"); 
    } else {
      setSortDirection("asc"); 
    }
  }

  const [filterRole, setFilterRole] = useState(isAdmin ? "no" : "user");
  const [selectedRole, setSelectedRole] = useState("Nincs");
  const [isOpenRoleSelector, setIsOpenRoleSelector] = useState(false);
  const optionsFilteringRole = [
    { value: "no", label: "Nincs" },
    { value: "moderator", label: "Moderátor" },
    { value: "user", label: "Felhasználó" },
  ];

  const handleSelectRole = (option) => {
    setSelectedRole(option.label);
    setIsOpenRoleSelector(false);

    if (option.value === "moderator") {
      setFilterRole("moderator"); 
    } else if (option.value === "user") {
      setFilterRole("user"); 
    } else {
      setFilterRole("no"); 
    }
  }

  const [selectedTotalElementNo, setSelectedTotalElementNo] = useState("5");
  const [isOpenPageNo, setIsOpenPageNo] = useState(false);
  const optionsPageNo = [
    { value: 5, label: "5" },
    { value: 10, label: "10" },
    { value: 25, label: "25" },
    { value: 50, label: "50" },
    { value: 100, label: "100" },
  ];

  const handleSelectPageNo = (option) => {
    setSelectedTotalElementNo(option.label);
    setIsOpenPageNo(false);
  }

  const handleManage = (id) => {
    navigate("/manage-user", {state: { id }});
  };


  const FETCH_USERS_URL = "/user-api/user/allUser";
  const [currentPageNumber, setCurrentPageNumber] = useState(0);
  const [usersData, setUsersData] = useState([]);
  const [totalPageNumber, setTotalPageNumer] = useState(1);

  useEffect(() => {
    if(!isUserSearching){
      const fetchUsers = async () => {
        const pageSize = parseInt(selectedTotalElementNo, 10);
  
        try{
          const response = await axiosInstance.get(FETCH_USERS_URL, {
            params:{
              pageNumber: currentPageNumber,
              pageSize: pageSize,
              sortDirection: sortDirection,
              filterByRole: filterRole
            },
            headers:{
              Authorization: `Bearer ${auth.accessToken}`
            },
            withCredentials: true
          });
          
          if(response.data.users.length === 0){
            if(filterRole === "moderator"){
              setModeratorExists(false);
              setTotalPageNumer(1);
            }
            else if(filterRole === "user"){
              setUserExists(false);
              setTotalPageNumer(1);
            }
            else{
              setModeratorExists(false);
              setUserExists(false);
              setTotalPageNumer(1);
            }
          }
          else{
            const data = response.data.users.map((item) => ({
              id: item.id,
              username: item.username
            }));
            setUsersData(data);
            const totalPageNo = response.data.totalPages;
            setTotalPageNumer(totalPageNo);
            setModeratorExists(true);
            setUserExists(true);
          }

        }catch(error){
          console.error(error);
          setModeratorExists(false);
          setUserExists(false);
          setTotalPageNumer(1);
        }
      };
      fetchUsers();
    }
  }, [currentPageNumber, selectedTotalElementNo, sortDirection, auth.accessToken, isUserSearching, filterRole]);

  const handlePageClick = (data) => {
    const selectedPage = data.selected;
    setCurrentPageNumber(selectedPage);
  };
  
  const startIndex = currentPageNumber * parseInt(selectedTotalElementNo, 10);

  const handleSearching = async (e) => {
    e.preventDefault();
    setIsUserSearching(true);
    if(searchingWord.trim()){
      try{
        const response = await axiosInstance.get(`/user-api/user/get/username/${searchingWord}`,
          {
            headers:{
              "Authorization": `Bearer ${auth.accessToken}`
            }
          });
          setResultId(response.data.id);
          setResultUsername(response.data.username);
          setToggleXMark(true);
          setTotalPageNumer(1);
          setIsResult(true);


        }catch(error){
            if(error.response){
                const { status, data } = error.response;
                if (status === 404 || status === 403) {
                    setIsResult(false);
                    setTotalPageNumer(1);
                    setToggleXMark(true); 
                } else {
                    alert(`Ismeretlen hiba: ${status} - ${data.error}`);
                }
            }
        }

    }else{
        setIsUserSearching(false);
        return;
    }
  }

  const resetSearching = () => {
    setResultId(0);
    setResultUsername("");
    setSearchingWord("");
    setIsUserSearching(false);
    setToggleXMark(false);
  };

  return (
    <div className='user-management-container'>
      <h1 className='um-title'>Felhasználók kezelése</h1>
      <hr className='um-title-line'/>
      <div className='um-search-and-sorting-group'>
        <div className='um-search-bar-container'>
          <button className="um-search-icon" onClick={handleSearching}>
            {<FontAwesomeIcon icon={faMagnifyingGlass} size="lg" style={{color: "#0886c1",}} />}  
          </button>
          <input type='text'
            name='um-searchbar'
            placeholder='Keresés'
            value={searchingWord}
            onChange={(e) => setSearchingWord(e.target.value)}
          >
          </input>
          {toggleXMark && (
            <button className="um-cross-icon" onClick={resetSearching}>
              {<FontAwesomeIcon icon={faXmark} size="lg" style={{color: "#0886c1",}} />}  
            </button>
          )}

        </div>
        
        <div className='page-sorting-container'>
          <p className='um-sorting-title'>Rendezés:</p>
          <div className="sorting-users">
            <div className={`sorting-users-selected ${
              isOpenDirectionSelector ? "sorting-users-arrow-active" : ""
              }`} onClick={() => setIsOpenDirectionSelector(!isOpenDirectionSelector)}
            > {selectedDirection}
            </div>
            {isOpenDirectionSelector && (
              <div className="sorting-users-items">
                {optionsSortingDirection.map((option) => (
                  <div
                    key={option.value}
                    onClick={() => handleSelectDirection(option)}
                    className={selectedDirection === option.label ? "same-as-selected-user" : ""}
                  > {option.label}
                  </div>
                ))}
              </div>
            )}
          </div>

          <div className="page-no">
            <div className={`page-no-selected ${
              isOpenPageNo ? "page-no-arrow-active" : ""
              }`} onClick={() => setIsOpenPageNo(!isOpenPageNo)}
            > {selectedTotalElementNo}
            </div>
            {isOpenPageNo && (
              <div className="page-no-items">
                {optionsPageNo.map((option) => (
                  <div
                    key={option.value}
                    onClick={() => handleSelectPageNo(option)}
                    className={selectedTotalElementNo === option.label ? "same-as-selected-page-no" : ""}
                  > {option.label}
                  </div>
                ))}
              </div>
            )}
          </div>
          
          {isAdmin && (
            <div className='filter-by-role-container'>
              <p className='um-filtering-title'>Szűrés:</p>
              <div className="filtering-users">
                <div className={`filtering-users-selected ${
                  isOpenRoleSelector ? "filtering-users-arrow-active" : ""
                  }`} onClick={() => setIsOpenRoleSelector(!isOpenRoleSelector)}
                > {selectedRole}
                </div>
                {isOpenRoleSelector && (
                  <div className="filtering-users-items">
                    {optionsFilteringRole.map((option) => (
                      <div
                        key={option.value}
                        onClick={() => handleSelectRole(option)}
                        className={selectedRole === option.label ? "same-as-selected-role" : ""}
                      > {option.label}
                      </div>
                    ))}
                  </div>
                )}
              </div>
            </div>
          )}
        </div>

      </div>

      {!isUserSearching && userExists && moderatorExists &&(
        <div className='users-table'>
          {usersData.map((item, index) => {
            const globalIndex = startIndex + index;
            return(
                <div className={(globalIndex + 1) % 2 === 1 ? "user-odd-row" : "user-even-row"}
                  key={item.id}
                >
                <span className='index'>{globalIndex + 1}.</span>
                <p className='username-in-users-table'>{item.username}</p>
                <button className="options-button" onClick={() => handleManage(item.id)}>
                  {<FontAwesomeIcon icon={faGear} size="lg" style={{color: "#0886c1"}} />}  
                </button>
              </div>
            );
          })}
        </div>
      )}

      {!isUserSearching && moderatorExists && !userExists &&(
        <p className='no-user-in-database'>Nincs regisztrált felhasználó!</p>
      )}

      {!isUserSearching && !moderatorExists && !userExists &&(
        <p className='nothing-in-database'>Nincs regisztrált felhasználó és moderátor!</p>
      )}

      {!isUserSearching && !moderatorExists && userExists && (
        <p className='no-moderator-in-database'>Nincs még kinevezve moderátor!</p>
      )}

      {isUserSearching && isResult &&(
        <div className='user-result-row'>
          <span className='index'>1.</span>
          <p className='username-in-users-table'>{resultUsername}</p>
          <button className="options-button" onClick={() => handleManage(resultId)}>
            {<FontAwesomeIcon icon={faGear} size="lg" style={{color: "#0886c1"}} />}  
          </button>
        </div>
      )}

      {isUserSearching && !isResult &&(
        <p className='no-user-result'>Nincs találat!</p>
      )}

      <ReactPaginate 
        previousLabel={'<<'}
        nextLabel={">>"}
        breakLabel={"..."}
        pageCount={totalPageNumber}
        onPageChange={handlePageClick}
        forcePage={currentPageNumber}
        marginPagesDisplayed={3}
        pageRangeDisplayed={6}
        containerClassName="custom-pagination"
        pageLinkClassName="page-link"
        activeClassName="custom-active"
        previousLinkClassName="previous-link" 
        nextLinkClassName="next-link"
        breakClassName="custom-break"
        previousClassName="custom-previous"
        nextClassName="custom-next"
      />
          
     
    </div>
  )
}

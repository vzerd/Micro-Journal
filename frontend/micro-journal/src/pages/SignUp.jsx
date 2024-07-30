import { useEffect, useState } from "react";
import { Link } from 'react-router-dom';
import Cookies from 'js-cookie';
import { useNavigate } from 'react-router-dom';
import axios from 'axios';

function SignUp(){
    const [name, setName] = useState("")
    const [email, setEmail] = useState("")
    const [password, setPassword] = useState("")
    const [confirmPassword, setConfirmPassword] = useState("")
    const [validationError, setValidationError] = useState("")
    const navigate = useNavigate();

    function handleSubmit(event){
        event.preventDefault();
        let errorMessage = validate(name, email, password, confirmPassword)
        if(errorMessage == "OK"){
            setValidationError("")
            axios.post('http://localhost:8080/v1/auth/sign-up', {
                name: name,
                email: email,
                password: password
              })
              .then(function (response) {
                Cookies.set('token', response.data.payload,  { expires: 365 })
                navigate('/home')
              })
              .catch(function (error) {
                if (error.response) {
                    alert(error.response.data.payload);
                }
              });
        }else{
            setValidationError(errorMessage)
        }
    }

    function validate(name, email, password, confirmPassword){
        if(name == "" && email == "" && password == "" && confirmPassword == ""){
            return "All fields are empty!"
        }else if(name == ""){
            return "Please enter the name."
        }else if(email == ""){
            return "Please enter the email."
        }else if(password == ""){
            return "Please enter the password."
        }else if(confirmPassword == ""){
            return "Please re-enter the password."
        }
        if(!(email.includes("@")) || !(email.includes("."))){
            return "Oops! Given email is not an email!"
        }else if(password.length < 8){
            return "Oops! Password length is less than 8!"
        }
        if(password !== confirmPassword){
            return "Oops! Passwords do not match!"
        }
        return "OK"
    }

    useEffect(() => {
        if(Cookies.get('token') !== undefined){
            navigate('/home')
        }
    })

    return (
        <>
            <div className="w-screen h-screen pt-36">
                <div className="float-left h-4/5 w-1/2 pt-20">
                    <h1 className="text-center px-20 py-5 font-serif text-6xl font-extrabold text-sky-300">Micro Journal</h1>
                    <h5 className="text-center font-serif text-xl font-semibold text-blue-100">Your Life, One Micro Note at a Time</h5>
                </div>
                <div className="float-left h-4/5 w-1/2">
                    <div className="w-1/2 p-10 pb-7 m-auto bg-slate-700 rounded-xl">
                        <h1 className="font-serif text-3xl font-extrabold text-white">Sign Up</h1>
                        <div className="w-full h-8">
                            <h6 className="text-orange-500">{validationError}</h6>
                        </div>
                        <form onSubmit={handleSubmit}>
                            <input className="w-full p-2 rounded-md" placeholder="Enter name here" type="text" onChange={(e) => setName(e.target.value)} />
                            <input className="w-full mt-4 p-2 rounded-md" placeholder="Enter email here" type="text" onChange={(e) => setEmail(e.target.value)} />
                            <input className="w-full mt-4 p-2 rounded-md" placeholder="Enter password here" type="password" onChange={(e) => setPassword(e.target.value)} />
                            <input className="w-full mt-4 p-2 rounded-md" placeholder="Re-enter password here" type="password" onChange={(e) => setConfirmPassword(e.target.value)} />                         
                            <input className="mt-5 bg-sky-700 p-2 rounded-md hover:bg-sky-600 text-white font-bold cursor-pointer" type="submit" value="Sign Up" /> 
                        </form>
                    </div>
                    <Link to="/signin"><h2 className="text-sky-500 text-center text-lg hover:underline hover:text-sky-300 cursor-pointer m-5">Already a user? click here to Sign in!</h2></Link>
                </div>
            </div>
        </>
    )
}

export default SignUp;